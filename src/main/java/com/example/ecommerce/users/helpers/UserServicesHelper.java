package com.example.ecommerce.users.helpers;

import com.example.ecommerce.shared.exceptions.AlreadyExistsException;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.models.Role;
import com.example.ecommerce.users.repository.RoleRepository;
import com.example.ecommerce.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServicesHelper {
  private final UserRepository repo;
  private final RoleRepository roleRepository;
  private final PasswordEncoder encoder;

  public void verifyExistingEmail(String email) {
    if (repo.findUserByEmail(email).isPresent()) {
      throw new AlreadyExistsException("Email already exists");
    }
  }

  public void verifyExistingName(String name) {
    if (repo.findUserByName(name).isPresent()) {
      throw new AlreadyExistsException("Email already exists");
    }
  }

  public Role getRoleByEnum(RoleEnum enum1) {
    return roleRepository
        .findByRoleEnum(enum1)
        .orElseThrow(() -> new NotFoundException("Role not found"));
  }

  public String encodePassword(String password) {
    return encoder.encode(password);
  }
}
