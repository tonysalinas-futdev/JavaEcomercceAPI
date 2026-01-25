package com.example.Ecomercce.cart.controllers;

import com.example.Ecomercce.cart.dto.CreateCartItem;
import com.example.Ecomercce.cart.models.Cart;
import com.example.Ecomercce.cart.models.CartItem;
import com.example.Ecomercce.cart.services.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping(name = "/api/v1/cart")
public class CartController {
  private final CartService cartService;

  @GetMapping()
  public ResponseEntity<List<CartItem>> getCartItems(Authentication authenticaton) {
    return ResponseEntity.ok(cartService.getAllItemsOfCart(authenticaton.getName()));
  }

  @PostMapping()
  public ResponseEntity<Cart> addItem(
      Authentication authentication, @RequestBody @Valid CreateCartItem dto) {
    Cart cart = cartService.addItem(authentication.getName(), dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(cart);
  }

  @DeleteMapping("/cartItem/{cartItemId}")
  public ResponseEntity<Cart> deleteItem(@PathVariable @Positive Long cartItem) {
    Cart cart = cartService.deleteItemFromCart(cartItem);
    return ResponseEntity.ok(cart);
  }

  @PatchMapping()
  public ResponseEntity<?> clearCart(Authentication authentication) {
    cartService.cleanCart(authentication.getName());
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/cartItem/{itemId}")
  public ResponseEntity<Cart> setItemQuantity(
      Authentication authentication,
      @PathVariable @Positive Long itemId,
      @Positive Integer quantity) {
    return ResponseEntity.ok(
        cartService.setItemQuantity(authentication.getName(), itemId, quantity));
  }
}
