package com.example.ecommerce.users.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
public record UpdatePasswordDTO(
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
                message = "Old password must be at least 8 characters long and include one uppercase letter, one lowercase letter, one number, and one special character among @$!%*?&#"
        )
        String oldPassword,

        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
                message = "New password must be at least 8 characters long and include one uppercase letter, one lowercase letter, one number, and one special character among @$!%*?&#"
        )
        String newPassword
) {}

