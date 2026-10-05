package com.example.ecommerce.auth.service;

import com.example.ecommerce.auth.model.Token;
import com.example.ecommerce.auth.repository.TokenRepository;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.users.models.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {
  private final TokenRepository repo;

  @Transactional
  public void saveUserTokenAndDeletePrevious(User user, String jwtToken) {
    Token token = Token.builder().revoked(false).expired(false).user(user).value(jwtToken).build();
    user.getToken().clear();
    user.getToken().add(token);
    repo.saveAndFlush(token);
  }

  public void revokeUserToken(User user) {
    Token token = user.getToken().getFirst();
    token.setRevoked(true);
    repo.saveAndFlush(token);
  }

  public Token getByValue(String value) {
    return repo.findByValue(value).orElseThrow(() -> new NotFoundException("Token not found"));
  }
}
