package com.example.Ecomercce.auth.utils;

import com.example.Ecomercce.auth.config.JwtProperties;
import com.example.Ecomercce.users.models.User;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
  private final SecretKeyProvider provider;
  private final JwtProperties properties;

  public String createAccessToken(User user) {
    return buildToken(user, properties.getExpiration());
  }

  public String createRefreshToken(User user) {
    return buildToken(user, properties.getRefreshTokenExpiration());
  }

  public String buildToken(User user, Long expiration) {
    return Jwts.builder()
        .id(UUID.randomUUID().toString())
        .claims(Map.of("id", user.getId().toString()))
        .subject(user.getEmail())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(provider.getTokenSecretKey())
        .compact();
  }
}
