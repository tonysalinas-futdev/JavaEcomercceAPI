package com.example.Ecomercce.auth.service;

import com.example.Ecomercce.auth.model.Token;
import com.example.Ecomercce.auth.repository.TokenRepository;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import com.example.Ecomercce.users.models.User;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {
  private final TokenRepository repo;

  public void saveUserToken(User user, String jwtToken) {
    Token token = Token.builder().revoked(false).expired(false).user(user).token(jwtToken).build();

    repo.saveAndFlush(token);
  }

  public void revokeAllTokensUser(User user) {
    List<Token> tokens = repo.getAllTokensFromUser(user);
    for (Token token : tokens) {
      token.setRevoked(true);
      token.setExpired(true);
    }
    repo.saveAllAndFlush(tokens);
  }

  public Token getToken(String value) {
    return repo.getByToken(value)
        .orElseThrow(() -> new NotFoundException("No se ha podido encontrar ese token"));
  }
}
