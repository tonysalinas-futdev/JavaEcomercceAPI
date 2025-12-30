package com.example.Ecomercce.shared.exceptions;

public class BasicException extends Exception {
  public BasicException(String message) {
    super(message);
  }

  public BasicException(String message, Throwable error) {
    super(message, error);
  }
}
