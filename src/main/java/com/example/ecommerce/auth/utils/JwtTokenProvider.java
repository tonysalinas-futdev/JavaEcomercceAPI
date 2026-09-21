package com.example.ecommerce.auth.utils;

import com.example.ecommerce.auth.config.JwtProperties;
import com.example.ecommerce.users.models.User;
import io.jsonwebtoken.Jwts;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
  private final SecretKeyProvider provider;
  private final JwtProperties properties;

    public String createAccessToken(User user) {
        return buildToken(user, properties.getExpiration(), "access_token");
    }

    public String createRefreshToken(User user) {
        return buildToken(user, properties.getRefreshTokenExpiration(), "refresh_token");
    }

    public String buildToken(User user, Long expiration, String type) {
        List<String> permissions= new ArrayList<>();

        user.getRoles().forEach(role -> {
            role.getPermissions().forEach(p-> permissions.add(p.getName()));
        });

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claim("token_type", type)
                .claim("id", user.getId().toString())
                .claim("roles",user.getRoles().stream().map(r->r.getRoleEnum().name()).toList())
                .claim("permissions",permissions)

                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(provider.getTokenSecretKey())
                .compact();
    }
}
