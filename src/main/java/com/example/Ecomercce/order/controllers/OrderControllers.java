package com.example.Ecomercce.order.controllers;

import com.example.Ecomercce.order.models.Order;
import com.example.Ecomercce.order.models.OrderStatus;
import com.example.Ecomercce.order.service.OrderService;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("api/v1/orders")
@AllArgsConstructor
public class OrderControllers {
  private final OrderService service;

  @PutMapping("/status/{id}")
  public ResponseEntity<?> updateStatus(@PathVariable Long id, OrderStatus status) {
    service.setStatus(id, status);
    return ResponseEntity.ok().build();
  }

  @GetMapping()
  public ResponseEntity<Page<Order>> getOrdersByStatus(
      OrderStatus status, @Positive Integer number, @Positive Integer size) {
    Page<Order> page = service.getByStatus(status, number, size);
    return ResponseEntity.ok(page);
  }
}
