package com.example.Ecomercce.integrationTests.globalConftest;

import com.example.Ecomercce.auth.dtos.AuthResponse;
import com.example.Ecomercce.auth.dtos.LoginDTO;
import com.example.Ecomercce.auth.service.AuthService;
import com.example.Ecomercce.users.dtos.CreateUser;
import com.example.Ecomercce.users.enums.RoleEnum;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.services.UserAdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GlobalConftest {
  private final UserAdminService service;
  private final AuthService authService;

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

  private User createUser() {
    CreateUser dto =
        CreateUser.builder()
            .name("user")
            .password("12345678Ja#")
            .email("user@gmail.com")
            .role(RoleEnum.USER)
            .build();
    return service.createUserByAdmin(dto);
  }

  private User createManager() {
    CreateUser dto =
        CreateUser.builder()
            .name("manager")
            .password("12345678Ja#")
            .email("manager@gmail.com")
            .role(RoleEnum.MANAGER)
            .build();
    return service.createUserByAdmin(dto);
  }

  public AuthResponse obtainAdminCredentials() {
    createAdmin();
    LoginDTO request = LoginDTO.builder().email("admin@gmail.com").password("12345678Ja#").build();
    AuthResponse response = authService.login(request);
    return response;
  }

  public AuthResponse obtainUserCredentials() {
    createUser();
    LoginDTO request = LoginDTO.builder().email("user@gmail.com").password("12345678Ja#").build();
    AuthResponse response = authService.login(request);
    return response;
  }

  public AuthResponse obtainManagerCredentials() {
    createManager();
    LoginDTO request =
        LoginDTO.builder().email("manager@gmail.com").password("12345678Ja#").build();
    AuthResponse response = authService.login(request);
    return response;
  }
}
