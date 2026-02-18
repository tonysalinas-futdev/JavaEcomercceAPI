package com.example.Ecomercce.products.utils;

import com.example.Ecomercce.products.model.Product;
import com.example.Ecomercce.products.services.ProductService;
import com.example.Ecomercce.shared.exceptions.InvalidRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductValidator {
  private ProductService productService;

  public Product validateAvaibilityAndStockAndReturn(Long productId, Integer quantity) {
    Product product = productService.getProductEntityByIdAndBlockRow(productId);

    if (product.getAvailable() == false) {
      throw new InvalidRequestException(product.getName() + " no disponible ahora mismo");
    }

    if (product.getStock() < quantity) {
      throw new InvalidRequestException(
          "Stock insuficiente para el producto " + product.getName() + " intente en otro momento");
    }

    return product;
  }
}
