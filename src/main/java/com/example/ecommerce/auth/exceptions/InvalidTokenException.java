package com.example.ecommerce.auth.exceptions;

import com.example.ecommerce.shared.exceptions.BasicException;

public class InvalidTokenException extends BasicException {
  public InvalidTokenException(String message) {
    super(message);
  }
}
