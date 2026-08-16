package com.peppernoodles.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.peppernoodles.auth.api.dto.AuthResponse;
import com.peppernoodles.auth.api.dto.LoginRequest;
import com.peppernoodles.auth.repository.RefreshTokenRepository;
import com.peppernoodles.auth.service.AuthService;
import com.peppernoodles.auth.service.SecureTokens;
import com.peppernoodles.common.error.ApiExceptions.UnauthorizedException;
import com.peppernoodles.support.IntegrationTest;
import com.peppernoodles.support.TestFixtures;
import com.peppernoodles.user.domain.RoleName;
import com.peppernoodles.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private TestFixtures fixtures;

    @Autowired
    private RefreshTokenRepository refreshTokens;

    @Autowired
    private com.peppernoodles.user.repository.UserRepository users;

    @Test
    @DisplayName("logs in with the correct password and issues both tokens")
    void logsIn() {
        User user = fixtures.enabledUser(RoleName.ROLE_USER);

        AuthResponse response =
                authService.login(new LoginRequest(user.getEmail(), TestFixtures.PASSWORD), "junit");

        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
        assertThat(response.user().roles()).containsExactly("ROLE_USER");
    }

    @Test
    @DisplayName("treats the e-mail as case-insensitive")
    void loginIsCaseInsensitive() {
        User user = fixtures.enabledUser(RoleName.ROLE_USER);

        AuthResponse response = authService.login(
                new LoginRequest(user.getEmail().toUpperCase(), TestFixtures.PASSWORD), "junit");

        assertThat(response.user().email()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("gives the same error for a wrong password and an unknown account")
    void doesNotLeakAccountExistence() {
        User user = fixtures.enabledUser(RoleName.ROLE_USER);

        String wrongPassword = messageOf(() ->
                authService.login(new LoginRequest(user.getEmail(), "Wrong123!"), "junit"));
        String unknownAccount = messageOf(() ->
                authService.login(new LoginRequest("nobody-here@example.com", "Wrong123!"), "junit"));

        assertThat(wrongPassword).isEqualTo(unknownAccount);
    }

    @Test
    @DisplayName("rotates the refresh token on use")
    void rotatesRefreshToken() {
        User user = fixtures.enabledUser(RoleName.ROLE_USER);
        AuthResponse first = authService.login(new LoginRequest(user.getEmail(), TestFixtures.PASSWORD), "junit");

        AuthResponse second = authService.refresh(first.refreshToken(), "junit");

        assertThat(second.refreshToken()).isNotEqualTo(first.refreshToken());
        assertThat(refreshTokens.findByTokenHash(SecureTokens.hash(first.refreshToken())))
                .get()
                .satisfies(token -> assertThat(token.isRevoked()).isTrue());
    }

    /**
     * Regression test. Replay detection revokes the whole token family and then
     * throws; when both happened in one transaction the throw rolled the
     * revocation back, leaving a leaked family fully usable.
     */
    @Test
    @DisplayName("replaying a used refresh token kills the whole family")
    void replayRevokesEntireFamily() {
        User user = fixtures.enabledUser(RoleName.ROLE_USER);
        AuthResponse first = authService.login(new LoginRequest(user.getEmail(), TestFixtures.PASSWORD), "junit");
        AuthResponse second = authService.refresh(first.refreshToken(), "junit");

        // An attacker replays the token that was already exchanged.
        assertThatThrownBy(() -> authService.refresh(first.refreshToken(), "attacker"))
                .isInstanceOf(UnauthorizedException.class);

        // The legitimate client's current token must also be dead.
        assertThatThrownBy(() -> authService.refresh(second.refreshToken(), "junit"))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    @DisplayName("refuses login for a suspended account")
    void refusesSuspendedAccount() {
        User user = fixtures.enabledUser(RoleName.ROLE_USER);
        user.suspend("testing");
        // The fixture returns a detached entity, so the change has to be flushed
        // explicitly — otherwise the test asserts against an unsuspended row.
        users.save(user);

        assertThatThrownBy(() ->
                        authService.login(new LoginRequest(user.getEmail(), TestFixtures.PASSWORD), "junit"))
                .isInstanceOf(UnauthorizedException.class);
    }

    private static String messageOf(Runnable action) {
        try {
            action.run();
            throw new AssertionError("expected the call to fail");
        } catch (UnauthorizedException e) {
            return e.getMessage();
        }
    }
}
