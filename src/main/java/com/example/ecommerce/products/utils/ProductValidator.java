package com.example.ecommerce.products.utils;

import com.example.ecommerce.products.model.Product;
import com.example.ecommerce.shared.exceptions.InvalidRequestException;

public class ProductValidator {

  public static void validateAvailabilityAndStock(Product product, Integer quantity) {

    if (product.getIsAvailable() == false) {
      throw new InvalidRequestException(product.getName() + " is not available");
    }

    if (product.getStock() < quantity) {
      throw new InvalidRequestException(
          "Insufficient stock for product " + product.getName() + " try again later");
    }
  }
}
