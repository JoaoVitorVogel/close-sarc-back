package com.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private TokenService tokenService;

    private Authentication authentication;
    private Jwt jwt;

    @BeforeEach
    void setUp() {
        // Configurar o issuer usando reflection
        ReflectionTestUtils.setField(tokenService, "issuer", "test-issuer");

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_user");
        authentication = new UsernamePasswordAuthenticationToken(
                "user@example.com",
                "password",
                Collections.singletonList(authority)
        );

        jwt = Jwt.withTokenValue("token123")
                .header("alg", "RS256")
                .claim("sub", "user@example.com")
                .claim("authorities", "ROLE_user")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(86400))
                .build();
    }

    @Test
    void testGenerateToken_ShouldReturnToken() {
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        String token = tokenService.generateToken(authentication);

        assertNotNull(token);
        assertEquals("token123", token);

        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void testGenerateToken_WithMultipleAuthorities_ShouldJoinAuthorities() {
        GrantedAuthority authority1 = new SimpleGrantedAuthority("ROLE_user");
        GrantedAuthority authority2 = new SimpleGrantedAuthority("ROLE_ADMIN");
        Authentication multiAuth = new UsernamePasswordAuthenticationToken(
                "user@example.com",
                "password",
                java.util.Arrays.asList(authority1, authority2)
        );

        Jwt multiJwt = Jwt.withTokenValue("token456")
                .header("alg", "RS256")
                .claim("sub", "user@example.com")
                .claim("authorities", "ROLE_user ROLE_ADMIN")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(86400))
                .build();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(multiJwt);

        String token = tokenService.generateToken(multiAuth);

        assertNotNull(token);
        assertEquals("token456", token);

        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void testGenerateToken_ShouldSetCorrectClaims() {
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        tokenService.generateToken(authentication);

        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
    }
}

