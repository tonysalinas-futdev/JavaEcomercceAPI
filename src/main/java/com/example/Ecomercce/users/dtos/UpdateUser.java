package com.example.Ecomercce.users.dtos;

import com.example.Ecomercce.users.enums.RoleEnum;
import jakarta.validation.constraints.Email;
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
public class UpdateUser {

  private String name;

  @Email private String email;

  private Boolean isEnabled;

  private Boolean credentialsNoExpired;

  private Boolean accountNoLocked;

  private RoleEnum role;
}
