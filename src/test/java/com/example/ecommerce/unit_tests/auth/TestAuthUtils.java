package com.example.ecommerce.unit_tests.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.ecommerce.auth.utils.JwtTokenParser;
import com.example.ecommerce.auth.utils.JwtTokenProvider;
import com.example.ecommerce.auth.utils.TokenExtractor;
import com.example.ecommerce.users.models.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class TestAuthUtils {
  @Autowired JwtTokenParser parser;
  @Autowired private JwtTokenProvider provider;
  @Autowired private TokenExtractor extractor;

  public User buildUserOfTests() {
    return User.builder()
        .id(4L)
        .name("Juan Antonio Chao Salinas")
        .email("kroosismo0202@gmail.com")
        .password("1234567Ab#")
        .build();
  }

  @Test
  public void shouldCreateAccessTokenSuccessfully() {
    User user = buildUserOfTests();

    String access_token = provider.createAccessToken(user);
    Claims payload = parser.parse(access_token);

    assertEquals("access_token", payload.get("token_type"));
    assertTrue(access_token.length() >= 10);
  }

  @Test
  public void shouldCreateRefreshTokenSuccessfully() {
    User user = buildUserOfTests();

    String refreshToken = provider.createRefreshToken(user);
    Object type = parser.parse(refreshToken).get("token_type");

    assertEquals("refresh_token", type.equals("refresh_token"));
    assertTrue(refreshToken.length() >= 10);
  }

  @Test
  public void shouldExtractInfoFromToken() {
    User user = buildUserOfTests();
    String access_token = provider.createAccessToken(user);

    Claims accessTokenPayload = parser.parse(access_token);
    String email = accessTokenPayload.getSubject();
    Object id = accessTokenPayload.get("id");

    assertEquals("kroosismo0202@gmail.com", email);
    assertThat(id).isEqualTo(user.getId().toString());
  }

  @Test
  public void shouldExtractTokenFromHeader() {
    User user = buildUserOfTests();
    String authHeader = "Bearer " + provider.createRefreshToken(user);
    String refreshToken = extractor.extract(authHeader);

    assertFalse(refreshToken.startsWith("Bearer "));
  }
}
