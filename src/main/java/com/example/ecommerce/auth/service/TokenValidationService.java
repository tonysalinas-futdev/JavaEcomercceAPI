package com.example.ecommerce.auth.service;

import com.example.ecommerce.auth.exceptions.InvalidTokenException;
import com.example.ecommerce.auth.model.Token;
import com.example.ecommerce.auth.utils.JwtTokenParser;
import io.jsonwebtoken.Claims;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenValidationService {

  private final TokenService tokenService;
  private final JwtTokenParser parser;

  public boolean isTokenExpired(String tokenValue) {
    Date today = new Date(System.currentTimeMillis());
    Date expiredDate = parser.parse(tokenValue).getExpiration();
    return today.after(expiredDate);
  }

  public void validateRefreshToken(Token token) {
    Claims claims = parser.parse(token.getValue());
    if (isTokenExpired(token.getValue())
        || token.isRevoked()
        || !claims.get("token_type").equals("refresh_token")) {
      throw new InvalidTokenException("Token invalid or expired");
    }
  }
}
