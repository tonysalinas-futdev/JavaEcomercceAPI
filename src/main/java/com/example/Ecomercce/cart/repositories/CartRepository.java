package com.example.Ecomercce.cart.repositories;

import com.example.Ecomercce.cart.models.Cart;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
  @Query("SELECT c FROM Cart c JOIN c.items ci WHERE ci.id= :cartItemId")
  Optional<Cart> findByCartItems_Id(@Param("cartItemId") Long cartItemId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT c FROM Cart c WHERE c.id= :cartId")
  Optional<Cart> findByIdForUpdate(@Param("cartId") Long cartId);
}
