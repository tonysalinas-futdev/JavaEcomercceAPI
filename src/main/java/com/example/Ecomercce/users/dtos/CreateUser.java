package com.example.Ecomercce.users.dtos;

import com.example.Ecomercce.users.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class CreateUser {
  @NotNull private String name;
  @Email private String email;

  @NotNull(
      message =
          "La contraseña debe tener al menos una letra mayúscula , una letra minúscula, un número y un caracter especial")
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$")
  private String password;

  private RoleEnum role;
}
