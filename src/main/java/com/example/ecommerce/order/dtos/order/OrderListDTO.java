package com.example.ecommerce.order.dtos.order;

import com.example.ecommerce.order.models.OrderStatus;
import lombok.Builder;

@Builder
public record OrderListDTO(Long id, Long userId, Double totalAmount, OrderStatus status) {}
