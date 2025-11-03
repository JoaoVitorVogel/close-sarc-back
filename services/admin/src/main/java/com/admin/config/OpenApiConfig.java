package com.admin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Admin API",
                version = "v1",
                description = "API de administração (professores, resources e classs)."
        )
)
public class OpenApiConfig { }
