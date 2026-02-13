package com.example.Ecomercce.cart.controllers;

import com.example.Ecomercce.cart.dto.CreateCartItem;
import com.example.Ecomercce.cart.models.Cart;
import com.example.Ecomercce.cart.services.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
  private final CartService cartService;

  @PostMapping()
  public ResponseEntity<Cart> addItem(
      Authentication authentication, @RequestBody @Valid CreateCartItem dto, Long cartId) {
    Cart cart = cartService.addItem(cartId, authentication.getName(), dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(cart);
  }

  @DeleteMapping("/cart/{cartId}/cartItem/{cartItemId}")
  public ResponseEntity<Cart> deleteItem(
      @PathVariable @Positive Long cartItem, @PathVariable Long cartId) {
    Cart cart = cartService.deleteItemFromCart(cartItem, cartId);
    return ResponseEntity.ok(cart);
  }

  @PatchMapping()
  public ResponseEntity<?> clearCart(Long cartId) {
    cartService.cleanCart(cartId);
    return ResponseEntity.noContent().build();
  }
}
