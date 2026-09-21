package com.example.ecommerce.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
public record UserProfileDTO(
        String name,
        String email
) {}

