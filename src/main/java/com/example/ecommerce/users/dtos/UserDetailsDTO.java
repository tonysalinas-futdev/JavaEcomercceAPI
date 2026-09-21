package com.example.ecommerce.users.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
public record UserDetailsDTO(
        Long id,
        String name,
        String email,
        LocalDateTime createdAt,
        Boolean isEnabled,
        Boolean accountNoLocked,
        Boolean credentialsNoExpired
) {}
