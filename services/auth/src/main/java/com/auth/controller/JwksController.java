package com.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JwksController {

    @Autowired
    private JWKSet jwkSet;

    @GetMapping("/oauth2/jwks")
    public Map<String, Object> jwks() {
        return this.jwkSet.toJSONObject();
    }
}