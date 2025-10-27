package com.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.codec.Base64;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class RsaKeyConfig {

    @Value("${jwt.key.public}")
    private Resource publicKeyResource;

    @Value("${jwt.key.private}")
    private Resource privateKeyResource;

    @Bean
    public RSAPublicKey jwtValidationKey() throws Exception {
        try (InputStream is = publicKeyResource.getInputStream()) {
            String publicKeyPEM = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PUBLIC KEY-----", "");
            byte[] encoded = Base64.decode(publicKeyPEM.getBytes());
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        }
    }

    @Bean
    public RSAPrivateKey jwtSigningKey() throws Exception {
        try (InputStream is = privateKeyResource.getInputStream()) {
            String privateKeyPEM = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PRIVATE KEY-----", "");
            byte[] encoded = Base64.decode(privateKeyPEM.getBytes());
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));
        }
    }
}