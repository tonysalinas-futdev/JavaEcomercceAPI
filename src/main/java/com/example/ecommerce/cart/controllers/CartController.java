package com.example.ecommerce.cart.controllers;

import com.example.ecommerce.cart.models.Cart;
import com.example.ecommerce.cart.services.CartService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
  private final CartService cartService;

  @GetMapping()
  public ResponseEntity<Cart> getMyCart(@AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(cartService.findByUserIdOrCreate(jwt.getClaim("id")));
  }


  public ResponseEntity<Void> addItem(
          @AuthenticationPrincipal Jwt jwt, @RequestParam Integer productId, @RequestParam Integer quantity) {
    cartService.addItem(jwt.getClaim("id"), productId, quantity);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteItem(@RequestParam Integer productId,@AuthenticationPrincipal Jwt jwt ) {
    cartService.deleteItem(productId, jwt.getClaim("id"));
    return ResponseEntity.ok().build();
  }

  @PatchMapping()
  public ResponseEntity<?> clearCart(@AuthenticationPrincipal Jwt jwt) {
    cartService.cleanItems(jwt.getClaim("id"));
    return ResponseEntity.noContent().build();
  }

  @PutMapping()
  public ResponseEntity<?> setProductQuantity(@AuthenticationPrincipal Jwt jwt, @RequestParam Integer productId, @RequestParam Integer quantity) {
      cartService.setItemQuantity(jwt.getClaim("id"),productId,quantity);
      return ResponseEntity.ok().build();
  }
}
