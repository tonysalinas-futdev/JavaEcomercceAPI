package com.example.ecommerce.start.listeners;

import com.example.ecommerce.products.model.Product;
import com.example.ecommerce.products.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateProductForTest {
  private final ProductRepository repo;

  @EventListener
  void createProduct(ApplicationReadyEvent event) {
    Product product = Product.builder().name("Product test").price(40.0D).build();

    repo.save(product);
  }
}
