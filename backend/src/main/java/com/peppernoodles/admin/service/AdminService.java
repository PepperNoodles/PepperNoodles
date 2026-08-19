package com.peppernoodles.admin.service;

import com.peppernoodles.admin.api.dto.AdminDtos.AuditLogDto;
import com.peppernoodles.admin.api.dto.AdminDtos.CreateInquiryRequest;
import com.peppernoodles.admin.api.dto.AdminDtos.DashboardDto;
import com.peppernoodles.admin.api.dto.AdminDtos.InquiryDto;
import com.peppernoodles.admin.api.dto.AdminDtos.ManagedRestaurantDto;
import com.peppernoodles.admin.api.dto.AdminDtos.ManagedUserDto;
import com.peppernoodles.admin.domain.AdminAuditLog;
import com.peppernoodles.admin.domain.AdminInquiry;
import com.peppernoodles.admin.domain.AdminInquiry.Status;
import com.peppernoodles.admin.repository.AdminAuditLogRepository;
import com.peppernoodles.admin.repository.AdminInquiryRepository;
import com.peppernoodles.admin.repository.AdminRestaurantRepository;
import com.peppernoodles.auth.service.RecaptchaVerifier;
import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import com.peppernoodles.common.mail.MailService;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.web.EmailAddress;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.restaurant.repository.RestaurantRepository;
import com.peppernoodles.shop.domain.OrderStatus;
import com.peppernoodles.shop.repository.OrderRepository;
import com.peppernoodles.shop.repository.ProductRepository;
import com.peppernoodles.user.domain.Role;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.repository.UserRepository;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 後台 — member management, suspension, and the contact-us inbox. */
@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final AdminInquiryRepository inquiries;
    private final AdminAuditLogRepository auditLog;
    private final UserRepository users;
    private final RestaurantRepository restaurants;
    private final AdminRestaurantRepository adminRestaurants;
    private final ProductRepository products;
    private final OrderRepository orders;
    private final MailService mailService;
    private final RecaptchaVerifier recaptchaVerifier;

    public AdminService(
            AdminInquiryRepository inquiries,
            AdminAuditLogRepository auditLog,
            UserRepository users,
            RestaurantRepository restaurants,
            AdminRestaurantRepository adminRestaurants,
            ProductRepository products,
            OrderRepository orders,
            MailService mailService,
            RecaptchaVerifier recaptchaVerifier) {
        this.inquiries = inquiries;
        this.auditLog = auditLog;
        this.users = users;
        this.restaurants = restaurants;
        this.adminRestaurants = adminRestaurants;
        this.products = products;
        this.orders = orders;
        this.mailService = mailService;
        this.recaptchaVerifier = recaptchaVerifier;
    }

    // --- 聯絡我們 ------------------------------------------------------------

    /** Open to anonymous callers, so it is reCAPTCHA-gated. */
    @Transactional
    public void submitInquiry(CreateInquiryRequest request, AuthenticatedUser caller) {
        recaptchaVerifier.verify(request.recaptchaToken());

        AdminInquiry inquiry = new AdminInquiry();
        inquiry.setBody(request.body());

        if (caller != null) {
            inquiry.setUser(users.findById(caller.id()).orElse(null));
        }
        if (inquiry.getUser() == null) {
            String email = EmailAddress.normalise(request.contactEmail());
            if (email == null || email.isBlank()) {
                throw new ValidationException("請留下聯絡信箱，以便我們回覆您。");
            }
            inquiry.setContactEmail(email);
        }

        inquiries.save(inquiry);
    }

    @Transactional(readOnly = true)
    public PageResponse<InquiryDto> listInquiries(Status status, Pageable pageable) {
        var page = status == null
                ? inquiries.findAllByOrderByCreatedAtDesc(pageable)
                : inquiries.findByStatusOrderByCreatedAtDesc(status, pageable);
        return PageResponse.of(page, AdminService::toInquiryDto);
    }

    @Transactional
    public void resolveInquiry(Long inquiryId, String note, AuthenticatedUser caller) {
        AdminInquiry inquiry =
                inquiries.findById(inquiryId).orElseThrow(() -> NotFoundException.of("訊息", inquiryId));
        if (inquiry.getStatus() == Status.RESOLVED) {
            throw new ConflictException("這則訊息已經處理過了。");
        }

        User admin = users.findById(caller.id()).orElseThrow(() -> NotFoundException.of("使用者", caller.id()));
        inquiry.resolve(admin, note);
        record(admin, "RESOLVE_INQUIRY", "admin_inquiry", inquiryId, null);
    }

    // --- member management ---------------------------------------------------

    @Transactional(readOnly = true)
    public PageResponse<ManagedUserDto> listUsers(Pageable pageable) {
        return PageResponse.of(users.findAll(pageable), AdminService::toManagedUserDto);
    }

    @Transactional
    public void suspend(Long userId, String reason, AuthenticatedUser caller) {
        User user = users.findById(userId).orElseThrow(() -> NotFoundException.of("使用者", userId));
        if (user.getId().equals(caller.id())) {
            throw new ConflictException("不能將自己停權。");
        }
        if (user.isSuspended()) {
            throw new ConflictException("此帳號已經是停權狀態。");
        }

        user.suspend(reason);
        User admin = users.findById(caller.id()).orElse(null);
        record(admin, "SUSPEND_USER", "user", userId, reason);

        mailService.sendAccountSuspended(user.getEmail(), reason);
        log.info("Admin {} suspended user {}", caller.id(), userId);
    }

    @Transactional
    public void reinstate(Long userId, AuthenticatedUser caller) {
        User user = users.findById(userId).orElseThrow(() -> NotFoundException.of("使用者", userId));
        if (!user.isSuspended()) {
            throw new ConflictException("此帳號並未被停權。");
        }

        user.reinstate();
        User admin = users.findById(caller.id()).orElse(null);
        record(admin, "REINSTATE_USER", "user", userId, null);

        mailService.sendAccountReinstated(user.getEmail());
        log.info("Admin {} reinstated user {}", caller.id(), userId);
    }

    // --- 餐廳管理 -------------------------------------------------------------

    /**
     * 後台餐廳查詢. Unlike the public catalogue this shows every restaurant with
     * its owner, so an admin can trace a listing back to an account.
     */
    @Transactional(readOnly = true)
    public PageResponse<ManagedRestaurantDto> listRestaurants(String query, Pageable pageable) {
        String q = (query == null || query.isBlank()) ? null : query.trim();
        return PageResponse.of(adminRestaurants.search(q, pageable), row -> new ManagedRestaurantDto(
                row.getId(),
                row.getName(),
                row.getAddress(),
                row.getContact(),
                row.getOwnerUserId(),
                row.getOwnerEmail(),
                row.getOwnerName(),
                row.getReviewCount() == null ? 0 : row.getReviewCount(),
                row.getRatingAverage(),
                row.getCreatedAt()));
    }

    // --- dashboard / audit ---------------------------------------------------

    @Transactional(readOnly = true)
    public DashboardDto dashboard() {
        return new DashboardDto(
                users.count(),
                users.findAll().stream().filter(User::isSuspended).count(),
                restaurants.count(),
                products.count(),
                orders.findByStatusOrderByCreatedAtDesc(OrderStatus.PENDING, Pageable.unpaged())
                        .getTotalElements(),
                inquiries.countByStatus(Status.OPEN));
    }

    @Transactional(readOnly = true)
    public PageResponse<AuditLogDto> auditLog(Pageable pageable) {
        return PageResponse.of(auditLog.findAllByOrderByCreatedAtDesc(pageable), entry -> new AuditLogDto(
                entry.getId(),
                entry.getActor() == null ? null : entry.getActor().getId(),
                entry.getActor() == null ? null : entry.getActor().getEmail(),
                entry.getAction(),
                entry.getTargetType(),
                entry.getTargetId(),
                entry.getDetail(),
                entry.getCreatedAt()));
    }

    private void record(User actor, String action, String targetType, Object targetId, String detail) {
        String json = detail == null ? null : "{\"reason\":%s}".formatted(quote(detail));
        auditLog.save(new AdminAuditLog(actor, action, targetType, targetId, json));
    }

    private static String quote(String raw) {
        return "\"" + raw.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }

    private static InquiryDto toInquiryDto(AdminInquiry i) {
        String submitter = null;
        if (i.getUser() != null) {
            submitter = i.getUser().getProfile() != null
                    ? i.getUser().getProfile().getNickname()
                    : i.getUser().getEmail();
        }
        return new InquiryDto(
                i.getId(),
                i.getUser() == null ? null : i.getUser().getId(),
                submitter,
                i.getContactEmail(),
                i.getBody(),
                i.getStatus().name(),
                i.getResolutionNote(),
                i.getCreatedAt(),
                i.getResolvedAt());
    }

    private static ManagedUserDto toManagedUserDto(User u) {
        String name = u.getProfile() != null
                ? u.getProfile().getNickname()
                : (u.getCompanyProfile() != null ? u.getCompanyProfile().getRealName() : u.getEmail());
        return new ManagedUserDto(
                u.getId(),
                u.getEmail(),
                name,
                u.getRoles().stream().map(Role::getName).toList(),
                u.isEnabled(),
                u.isSuspended(),
                u.getSuspendedReason(),
                u.getLastLoginAt(),
                u.getCreatedAt());
    }
}
