package com.example.ecommerce.products.utils;

import com.example.ecommerce.products.model.Product;
import com.example.ecommerce.shared.exceptions.InvalidRequestException;

public class ProductValidator {

  public static Product validateAvaibilityAndStockAndReturn(Product product, Integer quantity) {

    if (product.getAvailable() == false) {
      throw new InvalidRequestException(product.getName() + " is not available");
    }

    if (product.getStock() < quantity) {
      throw new InvalidRequestException(
          "Insufficient stock for product " + product.getName() + " try again later");
    }

    return product;
  }
}
