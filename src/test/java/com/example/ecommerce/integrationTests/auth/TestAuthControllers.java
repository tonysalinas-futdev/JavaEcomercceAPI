package com.example.ecommerce.integrationTests.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.integrationTests.globalconftest.GlobalConftest;
import com.example.ecommerce.users.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class TestAuthControllers {
  private final RestTemplate restTemplate = new RestTemplate();
  @Autowired private GlobalConftest conftest;

  private final String basicRoute = "http://localhost:8080/api/v1/auth/";

  @Test
  public void shouldReturn200AndSetCookiesWhenSignUp() {
    SignUpDTO request = new SignUpDTO("krooty24@gmail.com", "Tony Kroos", "Abcd12345#");
    ResponseEntity<?> response =
        restTemplate.postForEntity(basicRoute + "sign_up", request, Void.class);

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    assertTrue(response.getHeaders().containsKey("Set-Cookie"));
  }

  @Test
  public void shouldReturn200AndSetCookiesWhenLogin() {

    User user = conftest.createUser();

    LoginDTO loginDto = new LoginDTO(user.getEmail(), "12345678Ja#");
    ResponseEntity<?> response =
        restTemplate.postForEntity(basicRoute + "login", loginDto, Void.class);

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    assertTrue(response.getHeaders().containsKey("Set-Cookie"));
  }
}
