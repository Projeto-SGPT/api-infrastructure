package com.br.sgpt.infrastructure.service;

import com.br.sgpt.domain.entity.User;
import com.br.sgpt.domain.services.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String SECRET = "gPz+QGecY1WcULNjdME+5mMfdOqUNPiDhmuQzXjR6qw="; // Base64 (>= 256 bits)
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET));

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .claim("id", user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 dia
                .signWith(SIGNING_KEY, Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public User extractUser(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(SIGNING_KEY)
                .build();

        Claims claims = parser.parseSignedClaims(token).getPayload();

        return new User(
                UUID.fromString(claims.get("id", String.class)),
                claims.getSubject(),
                null,
                claims.get("email", String.class),
                claims.get("role", String.class)
        );
    }
}