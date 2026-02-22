package com.example.ecommerce.shared.exceptions;

public class BasicException extends RuntimeException {
  public BasicException(String message) {
    super(message);
  }

  public BasicException(String message, Throwable error) {
    super(message, error);
  }
}
