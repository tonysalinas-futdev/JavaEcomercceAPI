package com.example.Ecomercce.auth.exceptions;

import com.example.Ecomercce.shared.exceptions.BasicException;

public class InvalidTokenException extends BasicException {
  public InvalidTokenException(String message) {
    super(message);
  }
}
