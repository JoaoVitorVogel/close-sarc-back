package com.user.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "User API",
                version = "v1",
                description = "API para professores: recursos, reservas, eventos e turmas."
        )
)
public class OpenApiConfig {
    // Configuração simplificada - usando padrão do SpringDoc
}
