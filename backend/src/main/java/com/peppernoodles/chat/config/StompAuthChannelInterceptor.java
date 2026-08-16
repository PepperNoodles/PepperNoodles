package com.peppernoodles.chat.config;

import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.JwtClaims;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

/**
 * Authenticates the STOMP CONNECT frame from its {@code Authorization} header
 * and pins the resulting principal to the session.
 *
 * <p>Rejecting here matters: the legacy chat trusted a user id supplied in the
 * message payload, so any connected client could send messages as anyone.
 */
@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private static final Logger log = LoggerFactory.getLogger(StompAuthChannelInterceptor.class);

    private final JwtDecoder jwtDecoder;

    public StompAuthChannelInterceptor(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        String token = bearerToken(accessor.getNativeHeader("Authorization"));
        if (token == null) {
            throw new IllegalArgumentException("A STOMP CONNECT frame must carry an Authorization header.");
        }

        try {
            Jwt jwt = jwtDecoder.decode(token);
            List<String> roles = jwt.getClaimAsStringList(JwtClaims.ROLES);
            var authorities = (roles == null ? List.<String>of() : roles).stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            var principal = new AuthenticatedUser(
                    Long.valueOf(jwt.getSubject()),
                    jwt.getClaimAsString(JwtClaims.EMAIL),
                    roles == null ? Set.of() : Set.copyOf(roles));

            accessor.setUser(new StompPrincipal(principal, authorities));
            return message;
        } catch (JwtException e) {
            log.warn("Rejected STOMP CONNECT with an invalid token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid access token.");
        }
    }

    private static String bearerToken(List<String> headers) {
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        String value = headers.getFirst();
        return (value != null && value.startsWith("Bearer ")) ? value.substring(7) : null;
    }

    /** Carries the authenticated user as the STOMP session principal. */
    public static class StompPrincipal extends UsernamePasswordAuthenticationToken {

        private final AuthenticatedUser user;

        StompPrincipal(AuthenticatedUser user, List<SimpleGrantedAuthority> authorities) {
            super(user, null, authorities);
            this.user = user;
        }

        public AuthenticatedUser user() {
            return user;
        }

        /** Used by Spring as the /user/** destination key. */
        @Override
        public String getName() {
            return String.valueOf(user.id());
        }
    }
}
