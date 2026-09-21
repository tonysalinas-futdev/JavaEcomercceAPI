package com.example.ecommerce.auth.utils;

import com.example.ecommerce.auth.exceptions.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenExtractor {

  public String extractBearerToken(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer")) {
      throw new InvalidTokenException("Invalid token");
    }
     return authHeader.substring(7);

  }
}
