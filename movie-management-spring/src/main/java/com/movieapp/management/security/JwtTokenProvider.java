package com.movieapp.management.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String SECRET_FILE_PATH = "./jwt_secret.key";

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    private final SecretKey key;

    public JwtTokenProvider() {
        String secret = loadOrGenerateSecret();
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    private String loadOrGenerateSecret() {
        try {
            Path path = Paths.get(SECRET_FILE_PATH);

            if (Files.exists(path)) {
                return Files.readString(path);
            } else {
                String secret = Base64.getEncoder()
                        .encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
                Files.write(path, secret.getBytes(), StandardOpenOption.CREATE_NEW);
                return secret;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or generate JWT secret", e);
        }
    }

    public String generateToken(String username,  String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRolesFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            System.err.println("Invalid JWT Token: " + ex.getMessage());
        }
        return false;
    }
}
