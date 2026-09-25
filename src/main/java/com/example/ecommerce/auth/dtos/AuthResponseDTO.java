package com.example.ecommerce.auth.dtos;

import lombok.Builder;

@Builder
public record AuthResponseDTO(String accessToken, String refreshToken) {}
