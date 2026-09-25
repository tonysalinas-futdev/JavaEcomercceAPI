package com.example.ecommerce.users.dtos;

import com.example.ecommerce.users.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CreateUserDTO(
    @NotBlank(message = "Name cannot be blank") String name,
    @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format")
        String email,
    @NotNull(
            message =
                "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character among @$!%*?&#")
        @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
            message =
                "Password must be at least 8 characters long and include one uppercase letter, one lowercase letter, one number, and one special character among @$!%*?&#")
        String password,
    RoleEnum role) {}
