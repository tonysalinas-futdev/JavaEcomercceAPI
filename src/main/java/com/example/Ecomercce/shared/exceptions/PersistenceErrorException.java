package com.example.Ecomercce.shared.exceptions;

public class PersistenceErrorException extends BasicException {
  public PersistenceErrorException(String message) {
    super(message);
  }

  public PersistenceErrorException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
