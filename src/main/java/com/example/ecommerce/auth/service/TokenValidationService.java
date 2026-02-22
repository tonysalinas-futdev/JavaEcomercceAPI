package com.example.ecommerce.auth.service;

import com.example.ecommerce.auth.exceptions.InvalidTokenException;
import com.example.ecommerce.auth.model.Token;
import com.example.ecommerce.auth.utils.JwtTokenParser;
import com.example.ecommerce.users.models.User;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenValidationService {

  private final TokenService tokenService;
  private final JwtTokenParser parser;

  public boolean isTokenExpired(String token) {
    Date today = new Date(System.currentTimeMillis());
    Date expiredDate = parser.extractPayload(token).getExpiration();
    return today.after(expiredDate);
  }

  public boolean isTokenRevoked(String value) {
    Token token = tokenService.getToken(value);

    return token.isRevoked();
  }

  public void isTokenValid(String token, User user) {
    String userEmail = tokenService.getToken(token).getUser().getEmail();
    String emailInPayload = parser.extractPayload(token).getSubject();
    if (isTokenExpired(token) || isTokenRevoked(token) || !userEmail.equals(emailInPayload)) {
      throw new InvalidTokenException("Refresh token is invalid or expired");
    }
  }
}
