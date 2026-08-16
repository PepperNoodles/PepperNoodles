package com.peppernoodles.auth.api;

import com.peppernoodles.auth.api.dto.AuthResponse;
import com.peppernoodles.auth.api.dto.ForgotPasswordRequest;
import com.peppernoodles.auth.api.dto.LoginRequest;
import com.peppernoodles.auth.api.dto.RefreshRequest;
import com.peppernoodles.auth.api.dto.RegisterCompanyRequest;
import com.peppernoodles.auth.api.dto.RegisterRequest;
import com.peppernoodles.auth.api.dto.ResetPasswordRequest;
import com.peppernoodles.auth.api.dto.VerifyEmailRequest;
import com.peppernoodles.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "註冊、登入、Token 更新與密碼復原")
@SecurityRequirements // every endpoint here is reachable without a token
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "一般會員註冊",
            description = "Creates a disabled account and mails a verification link. "
                    + "Responds 202 because the account is not usable until the link is followed.")
    public void register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
    }

    @PostMapping("/register/company")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "企業會員註冊")
    public void registerCompany(@Valid @RequestBody RegisterCompanyRequest request) {
        authService.registerCompany(request);
    }

    @PostMapping("/login")
    @Operation(summary = "登入", description = "Returns an access token and a rotating refresh token.")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request,
            @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {
        return authService.login(request, userAgent);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "更新 Token",
            description = "Exchanges a refresh token for a new pair. The presented token is revoked; "
                    + "presenting it again revokes every session for the account.")
    public AuthResponse refresh(
            @Valid @RequestBody RefreshRequest request,
            @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {
        return authService.refresh(request.refreshToken(), userAgent);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "登出", description = "Idempotent — an unknown token still succeeds.")
    public void logout(@Valid @RequestBody RefreshRequest request) {
        authService.logout(request.refreshToken());
    }

    @PostMapping("/verify-email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "驗證信箱")
    public void verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request.token());
    }

    @PostMapping("/resend-verification")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "重寄驗證信",
            description = "Always responds 202, whether or not the address is registered.")
    public void resendVerification(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.resendVerification(request.email());
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "忘記密碼",
            description = "Always responds 202, so the endpoint cannot be used to test which "
                    + "addresses have accounts.")
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.email());
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "重設密碼", description = "Consumes the token and revokes all existing sessions.")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.token(), request.newPassword());
    }
}
