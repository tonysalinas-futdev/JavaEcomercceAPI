package com.example.Ecomercce.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SignUpDTO {
  @Email @NotNull private String email;

  @NotNull @NotBlank private String name;

  @NotNull
  @NotBlank(
      message =
          "La contraseña debe tener al menos una letra mayúscula , una letra minúscula, un número y un caracter especial")
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*#?&]{8,}$")
  private String password;
}
