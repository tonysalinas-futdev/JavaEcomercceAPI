package com.example.ecommerce.users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import lombok.Builder;

@Builder
public record UpdateUserProfileDTO(
       @Max(message = "The name cannot exceed 400 characters", value = 400) String name,
        @Email(message = "Invalid email format")  String email
) {

}
