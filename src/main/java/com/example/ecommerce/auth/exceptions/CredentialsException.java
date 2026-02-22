package com.example.ecommerce.auth.exceptions;

import com.example.ecommerce.shared.exceptions.BasicException;

public class CredentialsException extends BasicException {
  public CredentialsException(String message) {
    super(message);
  }
}
