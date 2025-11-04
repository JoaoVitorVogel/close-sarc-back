package com.auth.config;

import com.auth.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import com.nimbusds.jose.jwk.JWKSet;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() throws Exception {
        // Gerar chaves RSA para teste
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        publicKey = (RSAPublicKey) keyPair.getPublic();
        privateKey = (RSAPrivateKey) keyPair.getPrivate();

        securityConfig = new SecurityConfig();
        // Usar reflection para injetar as dependências
        java.lang.reflect.Field userDetailsServiceField = SecurityConfig.class.getDeclaredField("userDetailsService");
        userDetailsServiceField.setAccessible(true);
        userDetailsServiceField.set(securityConfig, userDetailsService);

        java.lang.reflect.Field publicKeyField = SecurityConfig.class.getDeclaredField("publicKey");
        publicKeyField.setAccessible(true);
        publicKeyField.set(securityConfig, publicKey);

        java.lang.reflect.Field privateKeyField = SecurityConfig.class.getDeclaredField("privateKey");
        privateKeyField.setAccessible(true);
        privateKeyField.set(securityConfig, privateKey);
    }

    @Test
    void testPasswordEncoder_ShouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder.matches("password", encoder.encode("password")));
    }

    @Test
    void testAuthenticationManager_ShouldReturnProviderManager() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        AuthenticationManager authManager = securityConfig.authenticationManager(passwordEncoder);

        assertNotNull(authManager);
    }

    @Test
    void testJwtEncoder_ShouldReturnJwtEncoder() {
        JwtEncoder encoder = securityConfig.jwtEncoder();

        assertNotNull(encoder);
    }

    @Test
    void testJwkSet_ShouldReturnJwkSet() {
        JWKSet jwkSet = securityConfig.jwkSet();

        assertNotNull(jwkSet);
    }

    @Test
    void testSecurityConfig_ShouldBeInstantiated() {
        // Testa que a configuração pode ser instanciada
        assertNotNull(securityConfig);
    }
}

