package com.example.Ecomercce.integrationTests.auth;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.Ecomercce.auth.dtos.AuthResponse;
import com.example.Ecomercce.auth.dtos.LoginDTO;
import com.example.Ecomercce.auth.dtos.SignUpDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestAuthControllers {
  private RestTemplate restTemplate = new RestTemplate();

  @Test
  @Sql("/data.sql")
  public void testSignUpController() {
    SignUpDTO request = new SignUpDTO("krooty24@gmail.com", "Tony Kroos", "Abcd12345#");
    AuthResponse response =
        restTemplate.postForObject(
            "http://localhost:8000/api/v1/auth/sign_up", request, AuthResponse.class);

    assertTrue(response.getAccessToken() != null);
  }

  @Test
  @Sql("/data.sql")
  public void testLoginController() {
    SignUpDTO request = new SignUpDTO("krooty@gmail.com", "Tony Kroos", "Abcd12345#");
    AuthResponse response =
        restTemplate.postForObject(
            "http://localhost:8000/api/v1/auth/sign_up", request, AuthResponse.class);

    LoginDTO loginDto = new LoginDTO(request.getEmail(), request.getPassword());
    AuthResponse response2 =
        restTemplate.postForObject(
            "http://localhost:8000/api/v1/auth/login", loginDto, AuthResponse.class);

    assertTrue(response2.getAccessToken() != null);
  }
}
