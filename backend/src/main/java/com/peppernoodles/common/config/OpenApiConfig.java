package com.peppernoodles.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI peppernoodlesOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("PepperNoodles API")
                        .description("""
                                REST API for 胡椒MAP — restaurant discovery, reviews, social features
                                and the food shop.

                                Authenticate with `POST /api/v1/auth/login`, then send the returned
                                access token as `Authorization: Bearer <token>`.
                                """)
                        .version("v1")
                        .license(new License().name("MIT")))
                .components(new Components().addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME));
    }
}
