package com.example.Ecomercce.auth.facade;

import com.example.Ecomercce.auth.dtos.AuthResponse;
import com.example.Ecomercce.auth.exceptions.InvalidTokenException;
import com.example.Ecomercce.auth.service.JwtService;
import com.example.Ecomercce.auth.service.JwtValidationService;
import com.example.Ecomercce.auth.utils.ExtractToken;
import com.example.Ecomercce.auth.utils.JwtTokenParser;
import com.example.Ecomercce.users.models.User;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtFacade {
  private final JwtValidationService validator;
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

    if (userEmail == null) {
      throw new InvalidTokenException("Subject not found");
    }

    return Map.of("userEmail", userEmail, "refreshToken", refreshToken);
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
