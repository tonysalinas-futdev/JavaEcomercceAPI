package com.example.ecommerce.products.exceptions;

import com.example.ecommerce.shared.exceptions.BasicException;

public class InsufficientStockException extends BasicException {
  public InsufficientStockException(String message) {
    super(message);
  }
}
