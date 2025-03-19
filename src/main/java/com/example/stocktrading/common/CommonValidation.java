package com.example.stocktrading.common;

import com.example.stocktrading.exception.CustomException;
import com.example.stocktrading.config.SecretKeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class CommonValidation {

    private final SecretKeyProvider secretKeyProvider;

    public CommonValidation(SecretKeyProvider secretKeyProvider) {
        this.secretKeyProvider = secretKeyProvider;
    }

    public String validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new CustomException("Unauthorized: User must be logged in!");
        }
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKeyProvider.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            throw new CustomException("Invalid or expired token");
        }
    }
}
