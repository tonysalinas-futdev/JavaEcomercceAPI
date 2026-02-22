package com.example.ecommerce.integrationTests.auth;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.ecommerce.auth.dtos.AuthResponse;
import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.integrationTests.globalconftest.GlobalConftest;
import com.example.ecommerce.users.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class TestAuthControllers {
  private RestTemplate restTemplate = new RestTemplate();
  @Autowired private GlobalConftest conftest;

  @Test
  public void shouldReturnAccessTokenWhenSignUp() {
    SignUpDTO request = new SignUpDTO("krooty24@gmail.com", "Tony Kroos", "Abcd12345#");
    AuthResponse response =
        restTemplate.postForObject(
            "http://localhost:8000/api/v1/auth/sign_up", request, AuthResponse.class);

    assertTrue(response.getAccessToken() != null);
  }

  @Test
  public void shouldLoginUser() {

    User user = conftest.createUser();

    LoginDTO loginDto = new LoginDTO(user.getEmail(), "12345678Ja#");
    AuthResponse response2 =
        restTemplate.postForObject(
            "http://localhost:8000/api/v1/auth/login", loginDto, AuthResponse.class);

    assertTrue(response2.getAccessToken() != null);
  }
}
