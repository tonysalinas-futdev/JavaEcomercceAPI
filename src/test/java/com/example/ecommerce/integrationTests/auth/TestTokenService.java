package com.example.ecommerce.integrationTests.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.example.ecommerce.auth.model.Token;
import com.example.ecommerce.auth.service.TokenService;
import com.example.ecommerce.auth.utils.JwtTokenProvider;
import com.example.ecommerce.users.models.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Transactional
public class TestTokenService {
  @Autowired private JwtConftest conftest;
  @Autowired private TokenService tokenService;

  @Autowired private JwtTokenProvider provider;

  @Test
  public void shouldSaveRefreshTokenSuccessfully() {
    User user = conftest.getSaveUser();
    String refreshToken = provider.createRefreshToken(user);

    tokenService.saveUserTokenAndDeletePrevious(user, refreshToken);
    Token token = user.getToken();

    assertFalse(token.isExpired());
    assertFalse(token.isRevoked());
    assertEquals(token.getValue(), refreshToken);
  }
}
