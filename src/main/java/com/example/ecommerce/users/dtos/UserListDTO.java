package com.example.ecommerce.users.dtos;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record UserListDTO(
         Long id,

         String name,

        String email,

         LocalDateTime createdAt
) {

}
