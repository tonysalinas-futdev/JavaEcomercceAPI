package com.example.ecommerce.messages.exceptions;

import com.example.ecommerce.shared.exceptions.BasicException;

public class EmailSenderException extends BasicException {
  public EmailSenderException(String message) {
    super(message);
  }
}
