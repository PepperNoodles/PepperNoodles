package com.peppernoodles.admin.api;

import com.peppernoodles.admin.api.dto.AdminDtos.AuditLogDto;
import com.peppernoodles.admin.api.dto.AdminDtos.DashboardDto;
import com.peppernoodles.admin.api.dto.AdminDtos.InquiryDto;
import com.peppernoodles.admin.api.dto.AdminDtos.ManagedUserDto;
import com.peppernoodles.admin.api.dto.AdminDtos.ResolveInquiryRequest;
import com.peppernoodles.admin.api.dto.AdminDtos.SuspendUserRequest;
import com.peppernoodles.admin.domain.AdminInquiry.Status;
import com.peppernoodles.admin.service.AdminService;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import com.peppernoodles.common.web.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Admin", description = "後台：會員管理、停權、聯絡我們")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "後台首頁統計")
    public DashboardDto dashboard() {
        return adminService.dashboard();
    }

    @GetMapping("/users")
    @Operation(summary = "會員清單")
    public PageResponse<ManagedUserDto> users(@PageableDefault(size = 20) Pageable pageable) {
        return adminService.listUsers(pageable);
    }

    @PostMapping("/users/{userId}/suspend")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "停權會員", description = "Mails the member and writes an audit entry.")
    public void suspend(
            @PathVariable Long userId,
            @Valid @RequestBody SuspendUserRequest request,
            @CurrentUser AuthenticatedUser caller) {
        adminService.suspend(userId, request.reason(), caller);
    }

    @PostMapping("/users/{userId}/reinstate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "恢復會員權限")
    public void reinstate(@PathVariable Long userId, @CurrentUser AuthenticatedUser caller) {
        adminService.reinstate(userId, caller);
    }

    @GetMapping("/inquiries")
    @Operation(summary = "聯絡我們收件匣")
    public PageResponse<InquiryDto> inquiries(
            @RequestParam(required = false) Status status, @PageableDefault(size = 20) Pageable pageable) {
        return adminService.listInquiries(status, pageable);
    }

    @PostMapping("/inquiries/{inquiryId}/resolve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "標記訊息已處理")
    public void resolveInquiry(
            @PathVariable Long inquiryId,
            @Valid @RequestBody ResolveInquiryRequest request,
            @CurrentUser AuthenticatedUser caller) {
        adminService.resolveInquiry(inquiryId, request.resolutionNote(), caller);
    }

    @GetMapping("/audit-log")
    @Operation(summary = "操作紀錄")
    public PageResponse<AuditLogDto> auditLog(@PageableDefault(size = 50) Pageable pageable) {
        return adminService.auditLog(pageable);
    }
}
