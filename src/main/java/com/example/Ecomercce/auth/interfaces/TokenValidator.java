package com.example.Ecomercce.auth.interfaces;

import com.example.Ecomercce.users.models.User;

public interface TokenValidator {

  public boolean isTokenExpired(String token);

  public boolean isTokenRevoked(String value);

  public void isTokenValid(String token, User user);
}
