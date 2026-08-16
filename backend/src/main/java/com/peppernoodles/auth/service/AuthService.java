package com.peppernoodles.auth.service;

import com.peppernoodles.auth.api.dto.AuthResponse;
import com.peppernoodles.auth.api.dto.AuthResponse.AuthenticatedUserSummary;
import com.peppernoodles.auth.api.dto.LoginRequest;
import com.peppernoodles.auth.api.dto.RegisterCompanyRequest;
import com.peppernoodles.auth.api.dto.RegisterRequest;
import com.peppernoodles.auth.domain.EmailVerificationToken;
import com.peppernoodles.auth.domain.PasswordResetToken;
import com.peppernoodles.auth.domain.RefreshToken;
import com.peppernoodles.auth.repository.EmailVerificationTokenRepository;
import com.peppernoodles.auth.repository.PasswordResetTokenRepository;
import com.peppernoodles.auth.repository.RefreshTokenRepository;
import com.peppernoodles.common.config.ApplicationProperties;
import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.common.error.ApiExceptions.UnauthorizedException;
import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import com.peppernoodles.common.mail.MailService;
import com.peppernoodles.common.web.EmailAddress;
import com.peppernoodles.tag.repository.FoodTagRepository;
import com.peppernoodles.user.domain.CompanyProfile;
import com.peppernoodles.user.domain.Role;
import com.peppernoodles.user.domain.RoleName;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.domain.UserProfile;
import com.peppernoodles.user.domain.UserStats;
import com.peppernoodles.user.repository.RoleRepository;
import com.peppernoodles.user.repository.UserRepository;
import com.peppernoodles.user.repository.UserStatsRepository;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Registration, login, token lifecycle, and account recovery.
 *
 * <p>Security properties this service upholds, none of which the legacy code did:
 *
 * <ul>
 *   <li>Login failures are indistinguishable from one another, so the endpoint
 *       cannot be used to enumerate accounts.
 *   <li>Refresh tokens rotate on every use, and re-presenting a revoked token
 *       revokes the entire family for that account.
 *   <li>Password reset and verification tokens are single-use, time-limited, and
 *       stored only as hashes.
 *   <li>Changing a password revokes every outstanding session.
 * </ul>
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository users;
    private final RoleRepository roles;
    private final UserStatsRepository userStats;
    private final FoodTagRepository foodTags;
    private final RefreshTokenRepository refreshTokens;
    private final RefreshTokenRevoker refreshTokenRevoker;
    private final EmailVerificationTokenRepository verificationTokens;
    private final PasswordResetTokenRepository resetTokens;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenIssuer accessTokenIssuer;
    private final MailService mailService;
    private final RecaptchaVerifier recaptchaVerifier;
    private final ApplicationProperties properties;

    public AuthService(
            UserRepository users,
            RoleRepository roles,
            UserStatsRepository userStats,
            FoodTagRepository foodTags,
            RefreshTokenRepository refreshTokens,
            RefreshTokenRevoker refreshTokenRevoker,
            EmailVerificationTokenRepository verificationTokens,
            PasswordResetTokenRepository resetTokens,
            PasswordEncoder passwordEncoder,
            AccessTokenIssuer accessTokenIssuer,
            MailService mailService,
            RecaptchaVerifier recaptchaVerifier,
            ApplicationProperties properties) {
        this.users = users;
        this.roles = roles;
        this.userStats = userStats;
        this.foodTags = foodTags;
        this.refreshTokens = refreshTokens;
        this.refreshTokenRevoker = refreshTokenRevoker;
        this.verificationTokens = verificationTokens;
        this.resetTokens = resetTokens;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenIssuer = accessTokenIssuer;
        this.mailService = mailService;
        this.recaptchaVerifier = recaptchaVerifier;
        this.properties = properties;
    }

    // --- registration --------------------------------------------------------

    @Transactional
    public void register(RegisterRequest request) {
        recaptchaVerifier.verify(request.recaptchaToken());

        String email = EmailAddress.normalise(request.email());
        requireEmailAvailable(email);

        User user = new User(email, passwordEncoder.encode(request.password()));
        user.setEnabled(false);
        user.addRole(roles.require(RoleName.ROLE_USER));

        if (request.foodTagIds() != null && !request.foodTagIds().isEmpty()) {
            user.setFoodTags(new LinkedHashSet<>(foodTags.findByIdIn(request.foodTagIds())));
        }

        UserProfile profile = new UserProfile();
        profile.setRealName(request.realName());
        profile.setNickname(request.nickname() != null ? request.nickname() : request.realName());
        profile.setPhone(request.phone());
        profile.setBirthDate(request.birthDate());
        profile.setGender(request.gender());
        profile.setLocation(request.location());
        user.setProfile(profile);

        users.save(user);
        userStats.save(new UserStats(user));

        issueVerificationEmail(user);
        log.info("Registered consumer account {}", email);
    }

    @Transactional
    public void registerCompany(RegisterCompanyRequest request) {
        recaptchaVerifier.verify(request.recaptchaToken());

        String email = EmailAddress.normalise(request.email());
        requireEmailAvailable(email);

        User user = new User(email, passwordEncoder.encode(request.password()));
        user.setEnabled(false);
        user.addRole(roles.require(RoleName.ROLE_COMPANY));

        CompanyProfile profile = new CompanyProfile();
        profile.setRealName(request.companyName());
        profile.setPhone(request.phone());
        profile.setLocation(request.location());
        user.setCompanyProfile(profile);

        users.save(user);
        userStats.save(new UserStats(user));

        issueVerificationEmail(user);
        log.info("Registered company account {}", email);
    }

    // --- login / tokens ------------------------------------------------------

    @Transactional
    public AuthResponse login(LoginRequest request, String userAgent) {
        String email = EmailAddress.normalise(request.email());

        User user = users.findWithRolesByEmail(email).orElse(null);

        if (user == null) {
            // Verify against a throwaway hash anyway: an unknown account and a
            // wrong password must cost the same, or the timing difference
            // becomes an account-enumeration oracle.
            passwordEncoder.matches(request.password(), DUMMY_HASH);
            log.info("Failed login attempt for unknown account {}", email);
            throw new UnauthorizedException("帳號或密碼錯誤。");
        }
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            log.info("Failed login attempt for user {}", user.getId());
            throw new UnauthorizedException("帳號或密碼錯誤。");
        }
        if (user.isSuspended()) {
            throw new UnauthorizedException("此帳號已被停權，請聯絡客服。");
        }
        if (!user.isEnabled()) {
            throw new UnauthorizedException("請先完成信箱驗證後再登入。");
        }

        user.setLastLoginAt(Instant.now());
        userStats.findByUserId(user.getId()).ifPresent(UserStats::recordLogin);

        return issueTokens(user, userAgent);
    }

    @Transactional
    public AuthResponse refresh(String rawRefreshToken, String userAgent) {
        String hash = SecureTokens.hash(rawRefreshToken);
        RefreshToken stored = refreshTokens
                .findByTokenHash(hash)
                .orElseThrow(() -> new UnauthorizedException("Refresh token is not valid."));

        if (stored.isRevoked()) {
            // The same token was already exchanged. Either it leaked or a client
            // replayed it; in both cases every session for this account is suspect.
            // The revocation must commit independently — throwing below would
            // otherwise roll it straight back.
            log.warn("Refresh token replay detected for user {}; revoking all sessions", stored.getUser().getId());
            refreshTokenRevoker.revokeAllForUser(stored.getUser().getId());
            throw new UnauthorizedException("Refresh token is not valid.");
        }
        if (stored.isExpired()) {
            throw new UnauthorizedException("Session has expired. Please sign in again.");
        }

        User user = users.findWithRolesById(stored.getUser().getId())
                .orElseThrow(() -> new UnauthorizedException("Account no longer exists."));
        if (user.isSuspended() || !user.isEnabled()) {
            throw new UnauthorizedException("This account is not active.");
        }

        stored.revoke();
        return issueTokens(user, userAgent);
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        // Idempotent: an unknown or already-revoked token is still a successful logout.
        refreshTokens
                .findByTokenHash(SecureTokens.hash(rawRefreshToken))
                .ifPresent(RefreshToken::revoke);
    }

    @Transactional
    public void logoutEverywhere(Long userId) {
        refreshTokens.revokeAllForUser(userId, Instant.now());
    }

    // --- e-mail verification -------------------------------------------------

    @Transactional
    public void verifyEmail(String rawToken) {
        EmailVerificationToken token = verificationTokens
                .findByTokenHash(SecureTokens.hash(rawToken))
                .orElseThrow(() -> new ValidationException("驗證連結無效或已被使用。"));

        if (!token.isUsable()) {
            throw new ValidationException("驗證連結已失效，請重新申請驗證信。");
        }

        token.consume();
        token.getUser().markEmailVerified();
        log.info("Verified e-mail for user {}", token.getUser().getId());
    }

    @Transactional
    public void resendVerification(String rawEmail) {
        String email = EmailAddress.normalise(rawEmail);
        // Always returns successfully — the response must not reveal registration status.
        users.findByEmail(email).ifPresent(user -> {
            if (!user.isEmailVerified()) {
                verificationTokens.consumeAllForUser(user.getId(), Instant.now());
                issueVerificationEmail(user);
            }
        });
    }

    // --- password recovery ---------------------------------------------------

    @Transactional
    public void forgotPassword(String rawEmail) {
        String email = EmailAddress.normalise(rawEmail);
        users.findByEmail(email).ifPresent(user -> {
            resetTokens.consumeAllForUser(user.getId(), Instant.now());

            String raw = SecureTokens.generate();
            resetTokens.save(new PasswordResetToken(
                    user,
                    SecureTokens.hash(raw),
                    Instant.now().plus(properties.mail().passwordResetTtl())));

            mailService.sendPasswordReset(user.getEmail(), raw);
            log.info("Issued password reset token for user {}", user.getId());
        });
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        PasswordResetToken token = resetTokens
                .findByTokenHash(SecureTokens.hash(rawToken))
                .orElseThrow(() -> new ValidationException("重設連結無效或已被使用。"));

        if (!token.isUsable()) {
            throw new ValidationException("重設連結已失效，請重新申請。");
        }

        User user = token.getUser();
        token.consume();
        user.setPasswordHash(passwordEncoder.encode(newPassword));

        // A password change ends every existing session.
        refreshTokens.revokeAllForUser(user.getId(), Instant.now());
        log.info("Password reset completed for user {}", user.getId());
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = users.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Account no longer exists."));

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new UnauthorizedException("目前的密碼不正確。");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        refreshTokens.revokeAllForUser(userId, Instant.now());
    }

    // --- internals -----------------------------------------------------------

    private void requireEmailAvailable(String email) {
        if (users.existsByEmail(email)) {
            throw new ConflictException("此電子信箱已被註冊。");
        }
    }

    private void issueVerificationEmail(User user) {
        String raw = SecureTokens.generate();
        verificationTokens.save(new EmailVerificationToken(
                user, SecureTokens.hash(raw), Instant.now().plus(properties.mail().verificationTtl())));
        mailService.sendEmailVerification(user.getEmail(), raw);
    }

    private AuthResponse issueTokens(User user, String userAgent) {
        var access = accessTokenIssuer.issue(user);

        String rawRefresh = SecureTokens.generate();
        refreshTokens.save(new RefreshToken(
                user,
                SecureTokens.hash(rawRefresh),
                Instant.now().plus(properties.jwt().refreshTokenTtl()),
                userAgent));

        return AuthResponse.of(
                access.value(), rawRefresh, access.expiresInSeconds(), access.expiresAt(), summarise(user));
    }

    private AuthenticatedUserSummary summarise(User user) {
        List<String> roleNames = user.getRoles().stream().map(Role::getName).toList();

        String displayName = user.getEmail();
        String avatarPath = null;
        if (user.getProfile() != null) {
            UserProfile profile = user.getProfile();
            displayName = profile.getNickname() != null ? profile.getNickname() : profile.getRealName();
            avatarPath = profile.getAvatarPath();
        } else if (user.getCompanyProfile() != null) {
            displayName = user.getCompanyProfile().getRealName();
            avatarPath = user.getCompanyProfile().getAvatarPath();
        }

        return new AuthenticatedUserSummary(user.getId(), user.getEmail(), displayName, avatarPath, roleNames);
    }

    /** A well-formed BCrypt hash of a value nobody knows, used to equalise login timing. */
    private static final String DUMMY_HASH =
            "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
}
