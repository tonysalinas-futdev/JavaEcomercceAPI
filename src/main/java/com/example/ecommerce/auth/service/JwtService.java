package com.example.ecommerce.auth.service;

import com.example.ecommerce.auth.dtos.AuthResponseDTO;
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
    tokenService.revokeUserToken(user);
    tokenService.saveUserTokenAndDeletePrevious(user, tokenValue);
  }

  public AuthResponseDTO buildAuthResponse(User user) {
    var accessToken = provider.createAccessToken(user);
    var refreshToken = provider.createRefreshToken(user);

    return AuthResponseDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
  }
}
