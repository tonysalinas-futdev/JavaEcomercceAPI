package com.example.Ecomercce.users.dtos;

import com.example.Ecomercce.users.models.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetails {
  private Long id;

  private String name;

  private String email;

  private LocalDateTime createdAt;

  private Boolean isEnabled;

  private Boolean accountNoLocked;

  private Boolean credentialsNoExpired;

  private Role role;
}
