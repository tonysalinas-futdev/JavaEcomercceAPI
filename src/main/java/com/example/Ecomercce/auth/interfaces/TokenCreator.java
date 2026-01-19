package com.example.Ecomercce.auth.interfaces;

import com.example.Ecomercce.users.models.User;

public interface TokenCreator {
  public String createAccessToken(User user);

  public String createRefreshToken(User user);

  public String buildToken(User user, Long expiration);
}
