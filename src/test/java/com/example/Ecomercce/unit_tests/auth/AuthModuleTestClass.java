package com.example.Ecomercce.unit_tests.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.Ecomercce.auth.utils.ExtractToken;
import com.example.Ecomercce.auth.utils.JwtTokenParser;
import com.example.Ecomercce.auth.utils.JwtTokenProvider;
import com.example.Ecomercce.users.models.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class AuthModuleTestClass {
  @Autowired private JwtTokenParser parser;
  @Autowired private JwtTokenProvider provider;
  @Autowired private ExtractToken extractor;

  public User buildUserOfTests() {
    return User.builder()
        .id(4L)
        .name("Juan Antonio Chao Salinas")
        .email("kroosismo0202@gmail.com")
        .password("1234567Ab#")
        .build();
  }

  @Test
  public void mustCreateAccessToken() {
    User user = buildUserOfTests();

    String access_token = provider.createAccessToken(user);
    Object type = parser.extractPayload(access_token).get("token_type");

    assertTrue(type.equals("access_token"));
    assertTrue(access_token.length() >= 10);
  }

  @Test
  public void mustCreateRefreshToken() {
    User user = buildUserOfTests();

    String refreshToken = provider.createRefreshToken(user);
    Object type = parser.extractPayload(refreshToken).get("token_type");

    assertTrue(type.equals("refresh_token"));
    assertTrue(refreshToken.length() >= 10);
  }

  @Test
  public void mustExtractDataOfToken() {
    User user = buildUserOfTests();
    String access_token = provider.createAccessToken(user);

    Claims accessTokenPayload = parser.extractPayload(access_token);
    String email = accessTokenPayload.getSubject();
    Object id = accessTokenPayload.get("id");

    assertTrue(email.equals("kroosismo0202@gmail.com"));
    assertThat(id).isEqualTo(user.getId().toString());
  }

  @Test
  public void shouldExtractTokenFromHeader() {
    User user = buildUserOfTests();
    String authHeader = "Bearer " + provider.createRefreshToken(user);
    String refreshToken = extractor.extractBearerToken(authHeader);

    assertTrue(!refreshToken.startsWith("Bearer "));
  }
}
