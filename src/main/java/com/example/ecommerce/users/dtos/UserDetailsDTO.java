package com.example.ecommerce.users.dtos;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UserDetailsDTO(
    Long id,
    String name,
    String email,
    LocalDateTime createdAt,
    Boolean isEnabled,
    Boolean accountNoLocked,
    Boolean credentialsNoExpired) {}
