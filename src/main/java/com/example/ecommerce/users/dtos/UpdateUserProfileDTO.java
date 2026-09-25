package com.example.ecommerce.users.dtos;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record UpdateUserProfileDTO(
    String name, @Email(message = "Invalid email format") String email) {}
