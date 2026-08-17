package com.peppernoodles.common.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.peppernoodles.common.config.ApplicationProperties;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Stateless JWT security.
 *
 * <p>Differences from the legacy {@code WebSecurityConfig} worth calling out:
 *
 * <ul>
 *   <li>CSRF stays disabled, but that is now <em>correct</em> rather than a hole:
 *       the API is stateless, holds no session cookie, and authenticates from an
 *       {@code Authorization} header a cross-site form cannot set.
 *   <li>Authorization is deny-by-default. The legacy chain ended in
 *       {@code anyRequest().permitAll()}, so every endpoint nobody remembered to
 *       list was public.
 *   <li>Role checks live next to the code they protect via {@code @PreAuthorize}
 *       rather than in one hand-maintained list of URL patterns.
 * </ul>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final ApplicationProperties properties;

    public SecurityConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Authentication and account recovery.
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Public browsing: the map, restaurant pages, the catalogue.
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/restaurants/**",
                                "/api/v1/map/**",
                                "/api/v1/discovery/**",
                                "/api/v1/food-tags/**",
                                "/api/v1/shop/products/**",
                                "/api/v1/shop/categories/**",
                                "/api/v1/forum/**")
                        .permitAll()

                        // 聯絡我們 — reachable while logged out.
                        .requestMatchers(HttpMethod.POST, "/api/v1/inquiries").permitAll()

                        // Server-to-server callbacks, verified by signature rather than by JWT.
                        .requestMatchers("/api/v1/payments/ecpay/callback", "/api/v1/linebot/webhook").permitAll()

                        // WebSocket handshake; the STOMP CONNECT frame carries the token.
                        .requestMatchers("/ws/**").permitAll()

                        // Docs and health.
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()

                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                        .decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Strength 10 matches the legacy hashes, so existing passwords keep working.
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(secretKey()).build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(properties.cors().allowedOrigins());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setExposedHeaders(List.of("Location"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        var authorities = new JwtGrantedAuthoritiesConverter();
        // Our claim already holds fully-qualified names such as ROLE_ADMIN.
        authorities.setAuthoritiesClaimName(JwtClaims.ROLES);
        authorities.setAuthorityPrefix("");

        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }

    private SecretKeySpec secretKey() {
        String configured = properties.jwt().secret();
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(configured);
        } catch (IllegalArgumentException notBase64) {
            keyBytes = configured.getBytes(StandardCharsets.UTF_8);
        }
        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "peppernoodles.jwt.secret must decode to at least 32 bytes for HS256; "
                            + "generate one with: openssl rand -base64 64");
        }
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }
}
