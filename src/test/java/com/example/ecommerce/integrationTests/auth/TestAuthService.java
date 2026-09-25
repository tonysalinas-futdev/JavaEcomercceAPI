package com.example.ecommerce.integrationTests.auth;

import static org.junit.jupiter.api.Assertions.*;

import com.example.ecommerce.auth.dtos.AuthResponseDTO;
import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.auth.service.AuthService;
import com.example.ecommerce.auth.utils.JwtTokenParser;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserQueryService;
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
public class TestAuthService {
  @Autowired private JwtConftest conftest;
  @Autowired private UserQueryService userQueryService;
  @Autowired private AuthService authService;
  @Autowired private JwtTokenParser parser;

  @Test
  public void shouldReturnAccessAndRefreshTokenWithUserData() {
    User user = conftest.getSaveUser();

    AuthResponseDTO authResponse = authService.login(new LoginDTO(user.getEmail(), "12345Abc#"));

    Claims accessTokenPayload = parser.parse(authResponse.accessToken());
    Claims refreshTokenPayload = parser.parse(authResponse.refreshToken());

    assertEquals(accessTokenPayload.getSubject(), user.getEmail());
    assertEquals(refreshTokenPayload.getSubject(), user.getEmail());
    assertEquals(refreshTokenPayload.get("id"), user.getId().toString());
    assertEquals(accessTokenPayload.get("id"), user.getId().toString());
  }

  @Test
  public void shouldFailLoginWithIncorrectCredentials() {
    conftest.getSaveUser();

    assertThrows(
        BadCredentialsException.class,
        () -> {
          authService.login(new LoginDTO("incorrectEmail@gmail.com", "1234567ABCd4#"));
        });
  }

  @Test
  public void shouldCreateUserAndReturnAccessTokenWithCorrectInfoWhenSignUp() {
    AuthResponseDTO authResponse =
        authService.signUp(
            new SignUpDTO("kroty0202@gmail.com", "Juan Antonio Chao Salinas", "Abcd12345#"));

    User user = userQueryService.findByEmailOrThrow("kroty0202@gmail.com");
    Claims accessToken = parser.parse(authResponse.accessToken());
    AuthResponseDTO loginTokens =
        authService.login(new LoginDTO("kroty0202@gmail.com", "Abcd12345#"));

    assertEquals("kroty0202@gmail.com", accessToken.getSubject());
    assertEquals("Juan Antonio Chao Salinas", user.getName());
    assertNotNull(loginTokens);
  }
}
