package com.example.ecommerce.integrationTests.auth;

import static org.junit.jupiter.api.Assertions.*;

import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.auth.service.TokenService;
import com.example.ecommerce.auth.utils.JwtTokenProvider;
import com.example.ecommerce.integrationTests.globalconftest.GlobalConftest;
import com.example.ecommerce.shared.dtos.error.ErrorResponseDTO;
import com.example.ecommerce.users.dtos.CreateUserDTO;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class TestAuthControllers {
  @Autowired private TestRestTemplate restTemplate;
  @Autowired private GlobalConftest conftest;
  @Autowired private AdminService service;
  @Autowired private JwtTokenProvider tokenProvider;
  @Autowired private TokenService tokenService;

  private final String basicRoute = "http://localhost:8080/api/v1/auth/";

  public User getSavedUserForTest() {
    CreateUserDTO data =
        CreateUserDTO.builder()
            .name("Juan Antonio Chao Salinas")
            .role(RoleEnum.USER)
            .password("12345Abcd#")
            .email("tony@gmail.com")
            .build();

    return service.createUserByAdmin(data);
  }

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

  @Test
  public void shouldFailRequestWhenCredentialsAreExpired() {
    User user = getSavedUserForTest();

    String expiredToken = tokenProvider.buildToken(user, 1L, "access_token");
    tokenService.saveUserTokenAndDeletePrevious(user, expiredToken);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(expiredToken);

    ResponseEntity<ErrorResponseDTO> response =
        restTemplate.exchange(
            "http://localhost:8080/api/v1/me/profile",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            ErrorResponseDTO.class);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("Token Expired", response.getBody().getMessage());
  }
}
