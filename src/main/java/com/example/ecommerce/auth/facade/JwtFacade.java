package com.example.ecommerce.auth.facade;

import com.example.ecommerce.auth.dtos.AuthResponse;
import com.example.ecommerce.auth.exceptions.InvalidTokenException;
import com.example.ecommerce.auth.service.JwtService;
import com.example.ecommerce.auth.service.TokenValidationService;
import com.example.ecommerce.auth.utils.ExtractToken;
import com.example.ecommerce.auth.utils.JwtTokenParser;
import com.example.ecommerce.users.models.User;
import io.jsonwebtoken.Claims;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtFacade {
  private final TokenValidationService validator;
  private final ExtractToken extractor;
  private final JwtTokenParser parser;
  private final JwtService jwtService;

  public AuthResponse saveTokenAndBuildResponse(User user) {
    AuthResponse authResponse = jwtService.buildAuthResponse(user);
    jwtService.saveNewUserToken(user, authResponse.getRefreshToken());
    return authResponse;
  }

  public Map<String, String> extractBearerTokenAndPayload(String authHeader) {
    String refreshToken = extractor.extractBearerToken(authHeader);

    String userEmail = parser.extractPayload(refreshToken).getSubject();

    String userId = parser.extractPayload(refreshToken).get("id").toString();

    if (userEmail == null) {
      throw new InvalidTokenException("Subject not found");
    }

    return Map.of("userEmail", userEmail, "refreshToken", refreshToken, "userId", userId);
  }

  public Claims extractClaims(String authHeader) {
    String value = extractor.extractBearerToken(authHeader);
    Claims claims = parser.extractPayload(value);
    return claims;
  }

  public boolean validateAccessToken(String token) {
    String value = extractor.extractBearerToken(token);

    if (validator.isTokenExpired(value)) {
      throw new InvalidTokenException("Expired token");
    }
    return true;
  }

  public void validateToken(String token, User user) {
    validator.isTokenValid(token, user);
  }
}
