package com.example.ecommerce.products.utils;

import com.example.ecommerce.products.model.Product;
import com.example.ecommerce.products.services.ProductService;
import com.example.ecommerce.shared.exceptions.InvalidRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductValidator {
  private ProductService productService;

  public Product validateAvaibilityAndStockAndReturn(Long productId, Integer quantity) {
    Product product = productService.getProductEntityByIdAndBlockRow(productId);

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
