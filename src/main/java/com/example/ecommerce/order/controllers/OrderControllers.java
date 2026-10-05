package com.example.ecommerce.order.controllers;

import com.example.ecommerce.order.dtos.order.OrderAllDetailsDTO;
import com.example.ecommerce.order.dtos.order.OrderListDTO;
import com.example.ecommerce.order.models.OrderStatus;
import com.example.ecommerce.order.service.OrderService;
import com.example.ecommerce.order.utils.OrderPaginationCriteria;
import com.example.ecommerce.shared.utils.PaginationCriteria;
import java.net.URI;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("api/v1/orders")
@AllArgsConstructor
public class OrderControllers {
  private final OrderService service;

  @GetMapping("/order/{orderId}")
  public ResponseEntity<OrderAllDetailsDTO> getById(@PathVariable Long orderId) {
    return ResponseEntity.ok(service.findByIdOrThrowAndReturnDto(orderId));
  }

  @PutMapping("/status/{id}")
  public ResponseEntity<?> updateStatus(@PathVariable Long id, OrderStatus status) {
    service.setStatus(id, status);
    return ResponseEntity.ok().build();
  }

  @GetMapping("status/{status}")
  public ResponseEntity<Page<OrderListDTO>> getOrdersByStatus(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "true") Boolean ascending,
      @RequestParam(required = false) OrderPaginationCriteria paginationCriteria,
      @PathVariable OrderStatus status) {

    PaginationCriteria<OrderPaginationCriteria> orderPaginationCriteria =
        new PaginationCriteria<>(page, size, paginationCriteria, ascending);
    return ResponseEntity.ok(service.findByStatus(status, orderPaginationCriteria));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<Page<OrderListDTO>> getOrdersFromUser(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "true") Boolean ascending,
      @RequestParam(required = false, defaultValue = "id")
          OrderPaginationCriteria paginationCriteria,
      @PathVariable Long userId) {

    PaginationCriteria<OrderPaginationCriteria> orderPaginationCriteria =
        new PaginationCriteria<>(page, size, paginationCriteria, ascending);
    return ResponseEntity.ok(service.findAllUserOrders(userId, orderPaginationCriteria));
  }

  @PostMapping
  public ResponseEntity<OrderAllDetailsDTO> createOrder(@RequestHeader("X-Request-ID") UUID requestId, Authentication authentication) {
    OrderAllDetailsDTO order = service.createOrder( requestId, authentication.getName());
    URI location = URI.create("api/v1/}" + order.id());
    return ResponseEntity.created(location).body(order);
  }
}
