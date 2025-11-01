package com.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "JWKS", description = "Publicação de chaves públicas (JWKS)")
public class JwksController {

    @Autowired
    private JWKSet jwkSet;

    @GetMapping("/oauth2/jwks")
    @Operation(
            summary = "Obter JWKS",
            description = "Retorna o conjunto de chaves públicas (JWKS) usadas para validação de assinaturas de tokens."
    )
    public Map<String, Object> jwks() {
        return this.jwkSet.toJSONObject();
    }
}
