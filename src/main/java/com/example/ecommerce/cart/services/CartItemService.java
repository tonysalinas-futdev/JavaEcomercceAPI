package com.example.ecommerce.cart.services;

import com.example.ecommerce.cart.models.CartItem;
import com.example.ecommerce.cart.repositories.CartItemRepository;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartItemService {
  private final CartItemRepository repo;

  public CartItem getCartItemById(Long cartItemId) {
    return repo.findById(cartItemId)
        .orElseThrow(() -> new NotFoundException("Cart Item not Found"));
  }

  public CartItem getByProductAndCartId(Long productId, Long cartId) {
    return repo.findByCartAndProductId(productId, cartId).orElse(null);
  }

  public CartItem setItemQuantity(Long cartItemId, Integer quantity) {
    CartItem item = getCartItemById(cartItemId);

    repo.save(item);
    return item;
  }
}
