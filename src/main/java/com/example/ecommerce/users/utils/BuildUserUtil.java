package com.example.ecommerce.users.utils;

import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.shared.exceptions.AlreadyExistsException;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.models.Role;
import com.example.ecommerce.users.repository.RoleRepository;
import com.example.ecommerce.users.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ecommerce.users.models.User;

@Service
@AllArgsConstructor
public class BuildUserUtil {

   public static User buildUser(@Valid SignUpDTO dto) {
    User user =
        User.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .isEnabled(true)
            .accountNoLocked(true)
            .build();
    return user;

  }
  }


