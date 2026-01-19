package com.example.Ecomercce.unit_tests.auth;

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
public class AuthModuleUnitTestClass {
  @Autowired private JwtTokenParser parser;
  @Autowired private JwtTokenProvider provider;
  @Autowired private ExtractToken extractor;

  @Test
  public void testCreateAccessToken() {
    User user =
        User.builder()
            .id(Long.valueOf("4"))
            .name("Juan Antonio Chao Salinas")
            .email("kroosismo0202@gmail.com")
            .password("1234567Ab#")
            .build();

    String access_token = provider.createAccessToken(user);
    assertTrue(access_token.length() >= 10);
  }

  @Test
  public void testCreateRefreshToken() {
    User user =
        User.builder()
            .id(Long.valueOf("5"))
            .name("Juan Antonio Chao Salinas")
            .email("kroosismo0202@gmail.com")
            .password("1234567Ab#")
            .build();
    String refreshToken = provider.createRefreshToken(user);
    assertTrue(refreshToken.length() >= 10);
  }

  @Test
  public void testExtractPayload() {

    User user =
        User.builder()
            .id(Long.valueOf("5"))
            .name("Juan Antonio Chao Salinas")
            .email("kroosismo0202@gmail.com")
            .password("1234567Ab#")
            .build();
    String refreshToken = provider.createRefreshToken(user);
    String access_token = provider.createAccessToken(user);

    Claims accessTokenPayload = parser.extractPayload(access_token);

    String email = accessTokenPayload.getSubject();
    Object id = accessTokenPayload.get("id");

    assertTrue(email.equals("kroosismo0202@gmail.com"));
    assertTrue(accessTokenPayload.getExpiration() != null);
  }

  @Test
  public void testExtractToken() {

    User user =
        User.builder()
            .id(Long.valueOf("5"))
            .name("Juan Antonio Chao Salinas")
            .email("kroosismo0202@gmail.com")
            .password("1234567Ab#")
            .build();

    String authHeader = "Bearer " + provider.createRefreshToken(user);

    String refreshToken = extractor.extractBearerToken(authHeader);

    assertTrue(!refreshToken.startsWith("Bearer "));
  }
}
