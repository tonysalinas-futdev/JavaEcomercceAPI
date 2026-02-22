package com.example.ecommerce.cart.controllers;

import com.example.ecommerce.cart.dto.CartDetailsDTO;
import com.example.ecommerce.cart.dto.CreateCartItem;
import com.example.ecommerce.cart.services.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping("/{id}")
  public ResponseEntity<CartDetailsDTO> getById(@Positive Long cartId) {
    return ResponseEntity.ok(cartService.getCartDetailsDTO(cartId));
  }

  @PostMapping()
  public ResponseEntity<CartDetailsDTO> addItem(
      Authentication authentication, @RequestBody @Valid CreateCartItem dto, Long cartId) {
    CartDetailsDTO cart = cartService.addItem(cartId, authentication.getName(), dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(cart);
  }

  @DeleteMapping("/cart/{cartId}/cartItem/{cartItemId}")
  public ResponseEntity<CartDetailsDTO> deleteItem(
      @PathVariable @Positive Long cartItem, @PathVariable Long cartId) {
    CartDetailsDTO cart = cartService.deleteItemFromCart(cartItem, cartId);
    return ResponseEntity.ok(cart);
  }

  @PatchMapping()
  public ResponseEntity<?> clearCart(Long cartId) {
    cartService.cleanCart(cartId);
    return ResponseEntity.noContent().build();
  }
}
