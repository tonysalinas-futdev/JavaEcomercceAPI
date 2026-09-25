package com.example.ecommerce.users.dtos;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record UpdateUserDTO(
    String name,
    @Email String email,
    Boolean isEnabled,
    Boolean credentialsNoExpired,
    Boolean accountNoLocked) {}
