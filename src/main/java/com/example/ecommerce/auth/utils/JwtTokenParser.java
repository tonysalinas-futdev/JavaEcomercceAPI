package com.example.ecommerce.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenParser {
  private final SecretKeyProvider provider;

  public Claims parse(String token) {
    return Jwts.parser()
        .verifyWith(provider.getTokenSecretKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
