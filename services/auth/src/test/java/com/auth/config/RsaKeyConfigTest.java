package com.auth.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RsaKeyConfigTest {

    private RsaKeyConfig rsaKeyConfig;
    private Resource publicKeyResource;
    private Resource privateKeyResource;

    @BeforeEach
    void setUp() throws Exception {
        rsaKeyConfig = new RsaKeyConfig();

        // Gerar chaves RSA para teste
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // Criar chaves PEM formatadas
        String publicKeyPEM = formatPublicKey(publicKey);
        String privateKeyPEM = formatPrivateKey(privateKey);

        // Criar recursos a partir das chaves PEM
        publicKeyResource = new ByteArrayResource(publicKeyPEM.getBytes());
        privateKeyResource = new ByteArrayResource(privateKeyPEM.getBytes());

        // Injetar recursos usando reflection
        ReflectionTestUtils.setField(rsaKeyConfig, "publicKeyResource", publicKeyResource);
        ReflectionTestUtils.setField(rsaKeyConfig, "privateKeyResource", privateKeyResource);
    }

    @Test
    void testJwtValidationKey_ShouldReturnRSAPublicKey() throws Exception {
        RSAPublicKey publicKey = rsaKeyConfig.jwtValidationKey();

        assertNotNull(publicKey);
        assertEquals("RSA", publicKey.getAlgorithm());
    }

    @Test
    void testJwtSigningKey_ShouldReturnRSAPrivateKey() throws Exception {
        RSAPrivateKey privateKey = rsaKeyConfig.jwtSigningKey();

        assertNotNull(privateKey);
        assertEquals("RSA", privateKey.getAlgorithm());
    }

    private String formatPublicKey(RSAPublicKey publicKey) {
        byte[] encoded = publicKey.getEncoded();
        String base64 = Base64.getEncoder().encodeToString(encoded);
        return "-----BEGIN PUBLIC KEY-----\n" + base64 + "\n-----END PUBLIC KEY-----";
    }

    private String formatPrivateKey(RSAPrivateKey privateKey) {
        byte[] encoded = privateKey.getEncoded();
        String base64 = Base64.getEncoder().encodeToString(encoded);
        return "-----BEGIN PRIVATE KEY-----\n" + base64 + "\n-----END PRIVATE KEY-----";
    }
}

