package com.example.Ecomercce.messages.exceptions;

import com.example.Ecomercce.shared.exceptions.BasicException;

public class EmailSenderException extends BasicException {
  public EmailSenderException(String message) {
    super(message);
  }
}
