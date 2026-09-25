package com.example.ecommerce.users.dtos;

import lombok.Builder;

@Builder
public record UserProfileDTO(String name, String email) {}
