package com.example.Ecomercce.integrationTests.auth;

import com.example.Ecomercce.users.enums.RoleEnum;
import com.example.Ecomercce.users.models.Role;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.repository.UserRepository;
import com.example.Ecomercce.users.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtConftest {
  private final UserRepository userRepo;
  private final RoleService roleService;
  private final PasswordEncoder encoder;

  public User returnSaveUser() {
    User user =
        User.builder()
            .name("Juan Antonio Chao Salinas")
            .email("tony@gmail.com")
            .password(encoder.encode("12345Abc#"))
            .build();

    Role role = roleService.getRoleByEnum(RoleEnum.USER);
    user.setRole(role);

    userRepo.saveAndFlush(user);
    return user;
  }
}
