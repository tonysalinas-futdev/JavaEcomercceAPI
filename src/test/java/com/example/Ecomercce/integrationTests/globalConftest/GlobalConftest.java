package com.example.Ecomercce.integrationTests.globalConftest;

import com.example.Ecomercce.auth.dtos.AuthResponse;
import com.example.Ecomercce.auth.dtos.LoginDTO;
import com.example.Ecomercce.users.dtos.CreateUser;
import com.example.Ecomercce.users.enums.RoleEnum;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.services.UserAdminService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GlobalConftest {

  private RestTemplate restTemplate = new RestTemplate();
  private final UserAdminService service;

  public GlobalConftest(UserAdminService service) {
    this.service = service;
  }

  private User createAdmin() {
    CreateUser dto =
        CreateUser.builder()
            .name("admin")
            .password("12345678Ja#")
            .email("admin@gmail.com")
            .role(RoleEnum.ADMIN)
            .build();
    return service.createUserByAdmin(dto);
  }

  public AuthResponse obtainCredentials() {
    createAdmin();
    LoginDTO request = LoginDTO.builder().email("admin@gmail.com").password("12345678Ja#").build();

    AuthResponse response =
        restTemplate.postForObject(
            "http://localhost:8000/api/v1/auth/login", request, AuthResponse.class);

    return response;
  }
}
