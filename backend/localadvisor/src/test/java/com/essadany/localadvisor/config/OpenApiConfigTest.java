package com.essadany.localadvisor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {

    private final OpenApiConfig config = new OpenApiConfig();

    @Test
    void openApiDefinesJwtBearerSecurityScheme() {
        OpenAPI openAPI = config.openAPI();

        SecurityScheme scheme = openAPI.getComponents()
                .getSecuritySchemes()
                .get(OpenApiConfig.BEARER_AUTH);

        assertThat(scheme).isNotNull();
        assertThat(scheme.getType()).isEqualTo(SecurityScheme.Type.HTTP);
        assertThat(scheme.getScheme()).isEqualTo("bearer");
        assertThat(scheme.getBearerFormat()).isEqualTo("JWT");
    }

    @Test
    void openApiAppliesBearerSchemeGlobally() {
        OpenAPI openAPI = config.openAPI();

        assertThat(openAPI.getSecurity())
                .anySatisfy(requirement -> assertThat(requirement).containsKey(OpenApiConfig.BEARER_AUTH));
    }
}
