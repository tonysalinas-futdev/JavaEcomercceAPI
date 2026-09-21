package com.example.ecommerce.auth.utils;

import com.example.ecommerce.auth.config.JwtProperties;
import com.example.ecommerce.auth.dtos.AuthResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CookieBuilder {
    private final JwtProperties properties;

    public ResponseCookie buildAccessTokenCookie(AuthResponseDTO authResponse){
        return   ResponseCookie.from("access_token",authResponse.accessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(properties.getExpiration())
                .build();
    }

    public ResponseCookie buildRefreshTokenCookie(AuthResponseDTO authResponse){
        return ResponseCookie.from("refresh_token",authResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(properties.getRefreshTokenExpiration())
                .build();
    }
}

