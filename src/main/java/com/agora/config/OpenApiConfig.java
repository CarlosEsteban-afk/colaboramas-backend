// En: com/agora/config/OpenApiConfig.java
package com.agora.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        SecurityScheme securityScheme = new SecurityScheme()
            .name(securitySchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .description("Introduce tu Token JWT aquí para autorizarte");

        return new OpenAPI()
            .components(
                new Components()
                    .addSecuritySchemes(securitySchemeName, securityScheme)
            )
            .addSecurityItem(
                new SecurityRequirement().addList(securitySchemeName)
            )
            .info(new Info()
                .title("Ágora API")
                .version("v1.0")
                .description("API para la red de colaboración académica Ágora.")
            );
    }
}