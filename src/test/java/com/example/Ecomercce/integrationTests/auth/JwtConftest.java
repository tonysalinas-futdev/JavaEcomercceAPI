package com.example.Ecomercce.integrationTests.auth;

import com.example.Ecomercce.users.enums.RoleEnum;
import com.example.Ecomercce.users.helpers.UserServicesHelper;
import com.example.Ecomercce.users.models.Role;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.repository.UserRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtConftest {
  private final UserRepository userRepo;
  private final UserServicesHelper helper;
  private final PasswordEncoder encoder;

  public User returnSaveUser() {
    Optional<User> existing = userRepo.findUserByName("Juan Antonio Chao Salinas");
    if (existing.isPresent()) {
      return existing.get();
    }
    User user =
        User.builder()
            .name("Juan Antonio Chao Salinas")
            .email("tony@gmail.com")
            .password(encoder.encode("12345Abc#"))
            .build();

    Role role = helper.getRoleByEnum(RoleEnum.USER);
    user.setRole(role);

    userRepo.saveAndFlush(user);
    return user;
  }
}
