package com.example.ecommerce.users.utils;

import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.users.models.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BuilderUserUtil {

  public static User build(@Valid SignUpDTO dto) {
   return
        User.builder()
            .name(dto.name())
            .email(dto.email())
            .isEnabled(true)
            .accountNoLocked(true)
            .build();

  }
}
