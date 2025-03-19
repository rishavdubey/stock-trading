package com.example.stocktrading.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class SecretKeyProvider {
    private final SecretKey secretKey;

    public SecretKeyProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = generateSecretKey(secret);
    }

    private SecretKey generateSecretKey(String secret) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = sha.digest(secret.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(keyBytes, "HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate secure key", e);
        }
    }



    public SecretKey getSecretKey() {
        return secretKey;
    }
}