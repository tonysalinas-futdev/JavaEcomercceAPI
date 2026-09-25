package com.example.ecommerce.auth.service;

import com.example.ecommerce.auth.model.Token;
import com.example.ecommerce.auth.repository.TokenRepository;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.users.models.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {
  private final TokenRepository repo;

  public void saveUserTokenAndDeletePrevious(User user, String jwtToken) {
    Token token = Token.builder().revoked(false).expired(false).user(user).value(jwtToken).build();
    if (user.getToken() != null) {
      repo.delete(user.getToken());
    }
    user.setToken(token);
    repo.saveAndFlush(token);
  }

  public void revokeUserToken(User user) {
    Token token = user.getToken();
    token.setRevoked(true);
    repo.saveAndFlush(token);
  }

  public Token getByValue(String value) {
    return repo.findByValue(value).orElseThrow(() -> new NotFoundException("Token not found"));
  }
}
