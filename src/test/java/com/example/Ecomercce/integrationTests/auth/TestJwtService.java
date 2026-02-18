package com.example.Ecomercce.integrationTests.auth;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.Ecomercce.auth.dtos.AuthResponse;
import com.example.Ecomercce.auth.dtos.LoginDTO;
import com.example.Ecomercce.auth.dtos.SignUpDTO;
import com.example.Ecomercce.auth.model.Token;
import com.example.Ecomercce.auth.service.AuthService;
import com.example.Ecomercce.auth.service.JwtService;
import com.example.Ecomercce.auth.utils.JwtTokenParser;
import com.example.Ecomercce.auth.utils.JwtTokenProvider;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.services.UserAdminService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Transactional
public class TestJwtService {
  @Autowired private JwtConftest conftest;
  @Autowired private JwtService service;
  @Autowired private JwtTokenProvider provider;
  @Autowired private UserAdminService userService;
  @Autowired private AuthService authService;
  @Autowired private JwtTokenParser parser;

  @Test
  public void shouldSaveTokenInDb() {
    User user = conftest.returnSaveUser();
    String refreshToken = provider.createRefreshToken(user);

    service.saveNewUserToken(user, refreshToken);
    Token token = user.getTokens().get(0);

    assertTrue(token.isExpired() == false);
    assertTrue(token.isRevoked() == false);
    assertTrue(token.getToken().equals(refreshToken));
  }

  @Test
  public void shouldReturnAccessAndRefreshTokenWithUserData() {
    User user = conftest.returnSaveUser();

    AuthResponse authResponse = authService.login(new LoginDTO(user.getEmail(), "12345Abc#"));

    Claims accessTokenPayload = parser.extractPayload(authResponse.getAccessToken());
    Claims refreshTokenPayload = parser.extractPayload(authResponse.getRefreshToken());

    assertTrue(accessTokenPayload.getSubject().equals(user.getEmail()));
    assertTrue(refreshTokenPayload.getSubject().equals(user.getEmail()));
    assertTrue(refreshTokenPayload.get("id").equals(user.getId().toString()));
    assertTrue(accessTokenPayload.get("id").equals(user.getId().toString()));
  }

  @Test
  public void shouldFailLoginWithIncorrectCredentials() {
    conftest.returnSaveUser();

    assertThrows(
        BadCredentialsException.class,
        () -> {
          authService.login(new LoginDTO("incorrectEmail@gmail.com", "1234567ABCd4#"));
        });
  }

  @Test
  public void shouldCreateUserAndReturnAccessToken() {
    AuthResponse authResponse =
        authService.signUp(
            new SignUpDTO("kroty0202@gmail.com", "Juan Antonio Chao Salinas", "Abcd12345#"));

    User user = userService.getUserByEmail("kroty0202@gmail.com");
    Claims accessToken = parser.extractPayload(authResponse.getAccessToken());
    AuthResponse loginTokens = authService.login(new LoginDTO("kroty0202@gmail.com", "Abcd12345#"));

    assertTrue(accessToken.getSubject().equals("kroty0202@gmail.com"));
    assertTrue(user.getName().equals("Juan Antonio Chao Salinas"));
    assertTrue(loginTokens != null);
  }
}
