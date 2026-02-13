package com.example.Ecomercce.cart.controllers;

import com.example.Ecomercce.cart.models.CartItem;
import com.example.Ecomercce.cart.services.CartItemService;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping("api/v1/items")
@RestController
@AllArgsConstructor
public class CartItemController {
  private final CartItemService service;

  @GetMapping("/{id}")
  public ResponseEntity<CartItem> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getCartItemById(id));
  }

  @PutMapping("/quantity/{id}")
  public ResponseEntity<?> setItemQuantity(
      @PathVariable @Positive Long id, @RequestBody @Positive Integer quantity) {
    return ResponseEntity.ok(service.setItemQuantity(id, quantity));
  }
}
