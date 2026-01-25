package com.example.Ecomercce.cart.repositories;

import com.example.Ecomercce.cart.models.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
  Optional<Cart> findByCartItemId(Long cartItemId);
}
