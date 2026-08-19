package com.peppernoodles.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.peppernoodles.admin.api.dto.AdminDtos.CreateInquiryRequest;
import com.peppernoodles.admin.domain.AdminInquiry.Status;
import com.peppernoodles.admin.service.AdminService;
import com.peppernoodles.auth.api.dto.LoginRequest;
import com.peppernoodles.auth.service.AuthService;
import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.common.error.ApiExceptions.UnauthorizedException;
import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import com.peppernoodles.support.IntegrationTest;
import com.peppernoodles.support.TestFixtures;
import com.peppernoodles.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class AdminServiceTest extends IntegrationTest {

    @Autowired private AdminService adminService;
    @Autowired private AuthService authService;
    @Autowired private TestFixtures fixtures;

    @Test
    @DisplayName("suspending an account stops it logging in, and reinstating restores it")
    void suspendAndReinstate() {
        User admin = fixtures.admin();
        User member = fixtures.consumer();

        adminService.suspend(member.getId(), "測試違規", fixtures.callerFor(admin));

        assertThatThrownBy(() ->
                        authService.login(new LoginRequest(member.getEmail(), TestFixtures.PASSWORD), "junit"))
                .isInstanceOf(UnauthorizedException.class);

        adminService.reinstate(member.getId(), fixtures.callerFor(admin));

        assertThat(authService
                        .login(new LoginRequest(member.getEmail(), TestFixtures.PASSWORD), "junit")
                        .accessToken())
                .isNotBlank();
    }

    /** Otherwise an admin could lock themselves out of the back office. */
    @Test
    @DisplayName("an admin cannot suspend their own account")
    void refusesSelfSuspension() {
        User admin = fixtures.admin();

        assertThatThrownBy(() -> adminService.suspend(admin.getId(), "oops", fixtures.callerFor(admin)))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("suspending twice is rejected rather than silently repeated")
    void refusesDoubleSuspension() {
        User admin = fixtures.admin();
        User member = fixtures.consumer();
        adminService.suspend(member.getId(), "第一次", fixtures.callerFor(admin));

        assertThatThrownBy(() -> adminService.suspend(member.getId(), "第二次", fixtures.callerFor(admin)))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("reinstating an account that was never suspended is rejected")
    void refusesPointlessReinstate() {
        User admin = fixtures.admin();
        User member = fixtures.consumer();

        assertThatThrownBy(() -> adminService.reinstate(member.getId(), fixtures.callerFor(admin)))
                .isInstanceOf(ConflictException.class);
    }

    /** The legacy back office kept no record of who suspended whom, or why. */
    @Test
    @DisplayName("suspension and reinstatement are written to the audit log")
    void writesAuditEntries() {
        User admin = fixtures.admin();
        User member = fixtures.consumer();

        adminService.suspend(member.getId(), "洗版", fixtures.callerFor(admin));
        adminService.reinstate(member.getId(), fixtures.callerFor(admin));

        var entries = adminService.auditLog(PageRequest.of(0, 100)).content().stream()
                .filter(e -> e.targetId().equals(String.valueOf(member.getId())))
                .toList();

        assertThat(entries).hasSize(2);
        assertThat(entries.stream().map(e -> e.action()))
                .containsExactlyInAnyOrder("SUSPEND_USER", "REINSTATE_USER");
        assertThat(entries).allSatisfy(e -> assertThat(e.actorEmail()).isEqualTo(admin.getEmail()));
        assertThat(entries.stream().filter(e -> e.action().equals("SUSPEND_USER")).findFirst().orElseThrow().detail())
                .contains("洗版");
    }

    // --- 聯絡我們 -------------------------------------------------------------

    @Test
    @DisplayName("a logged-in member's inquiry is attributed to them")
    void attributesInquiryToMember() {
        User member = fixtures.consumer();

        adminService.submitInquiry(
                new CreateInquiryRequest(null, "請問如何成為企業會員？", null), fixtures.callerFor(member));

        var inquiry = adminService.listInquiries(Status.OPEN, PageRequest.of(0, 100)).content().stream()
                .filter(i -> member.getId().equals(i.userId()))
                .findFirst()
                .orElseThrow();

        assertThat(inquiry.body()).isEqualTo("請問如何成為企業會員？");
        assertThat(inquiry.status()).isEqualTo("OPEN");
    }

    @Test
    @DisplayName("an anonymous inquiry must carry a contact address")
    void requiresContactEmailWhenAnonymous() {
        assertThatThrownBy(() -> adminService.submitInquiry(new CreateInquiryRequest(null, "沒有信箱", null), null))
                .isInstanceOf(ValidationException.class);

        adminService.submitInquiry(new CreateInquiryRequest("Visitor@Example.com", "有信箱", null), null);

        assertThat(adminService.listInquiries(Status.OPEN, PageRequest.of(0, 100)).content())
                .anySatisfy(i -> assertThat(i.contactEmail()).isEqualTo("visitor@example.com"));
    }

    @Test
    @DisplayName("resolving an inquiry moves it out of the open list and cannot be repeated")
    void resolvesInquiryOnce() {
        User admin = fixtures.admin();
        User member = fixtures.consumer();
        adminService.submitInquiry(new CreateInquiryRequest(null, "待處理問題", null), fixtures.callerFor(member));

        Long inquiryId = adminService.listInquiries(Status.OPEN, PageRequest.of(0, 100)).content().stream()
                .filter(i -> member.getId().equals(i.userId()))
                .findFirst()
                .orElseThrow()
                .id();

        adminService.resolveInquiry(inquiryId, "已回覆", fixtures.callerFor(admin));

        assertThat(adminService.listInquiries(Status.OPEN, PageRequest.of(0, 100)).content())
                .noneSatisfy(i -> assertThat(i.id()).isEqualTo(inquiryId));

        assertThatThrownBy(() -> adminService.resolveInquiry(inquiryId, "再一次", fixtures.callerFor(admin)))
                .isInstanceOf(ConflictException.class);
    }

    // --- 餐廳管理 -------------------------------------------------------------

    @Test
    @DisplayName("the restaurant list shows each listing with the account behind it")
    void listsRestaurantsWithOwners() {
        User owner = fixtures.owner();
        var restaurant = fixtures.restaurant(owner);

        var row = adminService.listRestaurants(restaurant.getName(), PageRequest.of(0, 20)).content().stream()
                .filter(r -> r.id().equals(restaurant.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(row.name()).isEqualTo(restaurant.getName());
        assertThat(row.ownerUserId()).isEqualTo(owner.getId());
        assertThat(row.ownerEmail()).isEqualTo(owner.getEmail());
        assertThat(row.ownerName()).isNotBlank();
    }

    @Test
    @DisplayName("the restaurant search matches on address and on the owner's e-mail")
    void searchesAddressAndOwnerEmail() {
        User owner = fixtures.owner();
        var restaurant = fixtures.restaurant(owner);

        assertThat(adminService.listRestaurants(restaurant.getAddress(), PageRequest.of(0, 20)).content())
                .anySatisfy(r -> assertThat(r.id()).isEqualTo(restaurant.getId()));

        assertThat(adminService.listRestaurants(owner.getEmail(), PageRequest.of(0, 20)).content())
                .anySatisfy(r -> assertThat(r.id()).isEqualTo(restaurant.getId()));
    }

    /** Regression guard: the filter binds an untyped NULL when no term is given. */
    @Test
    @DisplayName("an empty search returns every restaurant rather than failing")
    void emptySearchListsEverything() {
        fixtures.restaurant(fixtures.owner());

        assertThat(adminService.listRestaurants(null, PageRequest.of(0, 20)).totalElements())
                .isPositive();
        assertThat(adminService.listRestaurants("   ", PageRequest.of(0, 20)).totalElements())
                .isPositive();
    }

    @Test
    @DisplayName("the dashboard counts reflect what is in the database")
    void dashboardCounts() {
        User admin = fixtures.admin();
        var before = adminService.dashboard();

        User member = fixtures.consumer();
        adminService.suspend(member.getId(), "計數測試", fixtures.callerFor(admin));

        var after = adminService.dashboard();
        assertThat(after.totalUsers()).isGreaterThan(before.totalUsers());
        assertThat(after.suspendedUsers()).isEqualTo(before.suspendedUsers() + 1);
    }
}
