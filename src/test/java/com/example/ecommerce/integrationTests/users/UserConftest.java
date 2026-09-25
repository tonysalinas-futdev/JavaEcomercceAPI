package com.example.ecommerce.integrationTests.users;

import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.users.dtos.CreateUserDTO;
import com.example.ecommerce.users.enums.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserConftest {

  public CreateUserDTO buildCreateUserDto(
      String email, String password, String name, RoleEnum role) {
    return CreateUserDTO.builder().email(email).password(password).name(name).role(role).build();
  }

  public SignUpDTO buildSignUpDto(String name, String email, String password) {
    return SignUpDTO.builder().email(email).name(name).password(password).build();
  }
}
