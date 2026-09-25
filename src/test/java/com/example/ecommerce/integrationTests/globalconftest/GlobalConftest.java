package com.example.ecommerce.integrationTests.globalconftest;

import com.example.ecommerce.auth.dtos.AuthResponseDTO;
import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.service.AuthService;
import com.example.ecommerce.users.dtos.CreateUserDTO;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.repository.UserRepository;
import com.example.ecommerce.users.services.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GlobalConftest {
  private final AdminService service;
  private final AuthService authService;
  private final UserRepository userRepo;

  public User createAdmin() {
    CreateUserDTO dto =
        CreateUserDTO.builder()
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

    CreateUserDTO dto =
        CreateUserDTO.builder()
            .name("user")
            .password("12345678Ja#")
            .email("user@gmail.com")
            .role(RoleEnum.USER)
            .build();
    return service.createUserByAdmin(dto);
  }

  public User createManager() {
    CreateUserDTO dto =
        CreateUserDTO.builder()
            .name("manager")
            .password("12345678Ja#")
            .email("manager@gmail.com")
            .role(RoleEnum.MANAGER)
            .build();
    return service.createUserByAdmin(dto);
  }

  public AuthResponseDTO obtainAdminCredentials() {
    LoginDTO request = LoginDTO.builder().email("admin@gmail.com").password("12345678Ja#").build();
    AuthResponseDTO response = authService.login(request);
    return response;
  }

  public AuthResponseDTO obtainUserCredentials() {
    LoginDTO request = LoginDTO.builder().email("user@gmail.com").password("12345678Ja#").build();
    AuthResponseDTO response = authService.login(request);
    return response;
  }

  public AuthResponseDTO obtainManagerCredentials() {
    LoginDTO request =
        LoginDTO.builder().email("manager@gmail.com").password("12345678Ja#").build();
    AuthResponseDTO response = authService.login(request);
    return response;
  }
}
