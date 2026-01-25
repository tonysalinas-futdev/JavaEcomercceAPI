package com.example.Ecomercce.auth.events;

import com.example.Ecomercce.auth.dtos.LoginDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SignUpSuccess {
  private final LoginDTO userData;

  public LoginDTO getUserData() {
    return userData;
  }
}
