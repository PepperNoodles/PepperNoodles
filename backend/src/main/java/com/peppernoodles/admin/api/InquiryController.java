package com.peppernoodles.admin.api;

import com.peppernoodles.admin.api.dto.AdminDtos.CreateInquiryRequest;
import com.peppernoodles.admin.service.AdminService;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** 聯絡我們 — the public half of the admin inbox. */
@RestController
@RequestMapping("/api/v1/inquiries")
@Tag(name = "Contact us", description = "聯絡我們")
public class InquiryController {

    private final AdminService adminService;

    public InquiryController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "提交聯絡訊息",
            description = "Open to logged-out visitors, so it is reCAPTCHA-gated. "
                    + "Anonymous submissions must supply a contact e-mail.")
    public void submit(
            @Valid @RequestBody CreateInquiryRequest request, @CurrentUser AuthenticatedUser caller) {
        adminService.submitInquiry(request, caller);
    }
}
