package com.example.ecommerce.cart.services;

import com.example.ecommerce.cart.models.Cart;
import com.example.ecommerce.cart.repositories.CartRepository;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
  private final CartRepository repo;

  public Cart findByUserIdOrCreate(Long userId) {
    Optional<Cart> cart =repo.findById(userId.toString());
      return cart.orElseGet(() -> new Cart(userId.toString(), new HashMap<>()));
  }


  public void addItem(Long userId, Integer productId, Integer quantity) {
    Cart cart = findByUserIdOrCreate(userId);
    if (cart.getItems().containsKey(productId)) {
      return;
    }
    cart.getItems().put(productId, quantity);
    repo.save(cart);
  }

  public void setItemQuantity(Long userId, Integer productId, Integer quantity){
      Optional<Cart> cart = repo.findById(userId.toString());
      if (cart.isPresent() && cart.get().getItems().containsKey(productId)){
          cart.get().getItems().remove(productId);
          cart.get().getItems().put(productId,quantity);
          repo.save(cart.get());
      }


  }

  public void cleanItems(Long userId) {
    Cart cart = findByUserIdOrCreate(userId);
    cart.setItems(new HashMap<>());
    repo.save(cart);
  }

  public void deleteItem(Integer productId, Long userId) {
    Cart cart = findByUserIdOrCreate(userId);
    cart.getItems().remove(productId);
    repo.save(cart);
  }
}
