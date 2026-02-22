package com.example.ecommerce.auth.service;

import com.example.ecommerce.auth.dtos.AuthResponse;
import com.example.ecommerce.auth.utils.JwtTokenProvider;
import com.example.ecommerce.users.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
  private final TokenService tokenService;
  private final JwtTokenProvider provider;

  public void saveNewUserToken(User user, String tokenValue) {
    tokenService.revokeAllTokensUser(user);
    tokenService.saveUserToken(user, tokenValue);
  }

  public AuthResponse buildAuthResponse(User user) {
    var accessToken = provider.createAccessToken(user);
    var refreshToken = provider.createRefreshToken(user);

    return AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
  }
}
