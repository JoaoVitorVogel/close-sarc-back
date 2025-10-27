package com.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Autowired
    private JwtEncoder jwtEncoder;

    // 24 horas de expiração
    private final long EXPIRATION_TIME_MS = 86400000;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        // Pega as "roles" (ex: "ROLE_ADMIN")
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:8080") // O emissor do token
                .issuedAt(now)
                .expiresAt(now.plusMillis(EXPIRATION_TIME_MS))
                .subject(authentication.getName()) // O email
                .claim("authorities", authorities) // Claim customizada para roles
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}