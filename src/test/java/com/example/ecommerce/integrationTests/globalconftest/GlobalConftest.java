package com.example.ecommerce.integrationTests.globalconftest;

import com.example.ecommerce.auth.dtos.AuthResponse;
import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.service.AuthService;
import com.example.ecommerce.users.dtos.CreateUser;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.repository.UserRepository;
import com.example.ecommerce.users.services.UserAdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GlobalConftest {
  private final UserAdminService service;
  private final AuthService authService;
  private final UserRepository userRepo;

  public User createAdmin() {
    CreateUser dto =
        CreateUser.builder()
            .name("admin")
            .password("12345678Ja#")
            .email("admin@gmail.com")
            .role(RoleEnum.ADMIN)
            .build();
    return service.createUserByAdmin(dto);
  }

  public User createUser() {
    var user = userRepo.findUserByEmail("user@gmail.com");
    if (user.isPresent()) {
      return user.get();
    }

    CreateUser dto =
        CreateUser.builder()
            .name("user")
            .password("12345678Ja#")
            .email("user@gmail.com")
            .role(RoleEnum.USER)
            .build();
    return service.createUserByAdmin(dto);
  }

  public User createManager() {
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
    LoginDTO request = LoginDTO.builder().email("admin@gmail.com").password("12345678Ja#").build();
    AuthResponse response = authService.login(request);
    return response;
  }

  public AuthResponse obtainUserCredentials() {
    LoginDTO request = LoginDTO.builder().email("user@gmail.com").password("12345678Ja#").build();
    AuthResponse response = authService.login(request);
    return response;
  }

  public AuthResponse obtainManagerCredentials() {
    LoginDTO request =
        LoginDTO.builder().email("manager@gmail.com").password("12345678Ja#").build();
    AuthResponse response = authService.login(request);
    return response;
  }
}
