package com.example.ecommerce.cart.repositories;

import com.example.ecommerce.cart.models.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  @Query("SELECT c FROM CartItem c WHERE c.product.id= :productId AND c.cart.id= :cartId")
  public Optional<CartItem> findByCartAndProductId(
      @Param("productId") Long productId, @Param("cartId") Long cartId);
}
