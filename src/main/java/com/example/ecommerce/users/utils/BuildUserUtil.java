package com.example.ecommerce.users.utils;

import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.users.models.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
