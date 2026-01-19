package com.example.Ecomercce.auth.utils;

import com.example.Ecomercce.auth.exceptions.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExtractToken {

  public String extractBearerToken(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer")) {
      throw new InvalidTokenException("Invalid token");
    }
    String refreshToken = authHeader.substring(7);
    return refreshToken;
  }
}
