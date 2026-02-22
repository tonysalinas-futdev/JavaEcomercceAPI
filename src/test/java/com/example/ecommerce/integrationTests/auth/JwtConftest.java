package com.example.ecommerce.integrationTests.auth;

import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.models.Role;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.repository.RoleRepository;
import com.example.ecommerce.users.repository.UserRepository;
import com.example.ecommerce.users.utils.BuildUserUtil;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtConftest {
  private final UserRepository userRepo;
  private final RoleRepository roleRepository;
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

    Role role = roleRepository.findByRoleEnum(RoleEnum.USER).orElseThrow(()-> new NotFoundException("Role USER not found"));
    user.setRole(role);

    userRepo.saveAndFlush(user);
    return user;
  }
}
