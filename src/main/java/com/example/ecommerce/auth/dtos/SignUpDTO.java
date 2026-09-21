package com.example.ecommerce.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
public record SignUpDTO(
        @Email(message = "Invalid email format") @NotBlank(message = "The email cannot be null or empty") String email,

        @NotBlank(message = "The name cannot be null or empty") String name,

@NotBlank(
        message =
                "The password must contain at least one uppercase letter, one lowercase letter, one number, and one special character among @$!%*?&#")
@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*#?&]{8,}$")
    String password
) {

}
