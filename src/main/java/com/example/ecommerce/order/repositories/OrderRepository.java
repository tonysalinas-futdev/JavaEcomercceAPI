package com.example.ecommerce.order.repositories;

import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.order.models.OrderStatus;
import com.example.ecommerce.users.models.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  public Optional<Order> findByUser(User user);

  Page<Order> findByStatus(OrderStatus status, Pageable pageable);

  @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.user.id= :userId ")
  public Page<Order> findByUserId(@Param("userId") Long userId, Pageable pageable);

  @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.id= :orderId")
  public Optional<Order> findByIdAndLoadUser(@Param("orderId") Long orderId);
}
