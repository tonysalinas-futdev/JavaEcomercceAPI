package com.example.ecommerce.order.dtos.order;

import com.example.ecommerce.order.dtos.orderdetails.OrderDetailsDTO;
import com.example.ecommerce.order.models.OrderStatus;
import com.example.ecommerce.users.models.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record OrderAllDetailsDTO(
    Long id,
    User user,
    Double totalAmount,
    OrderStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<OrderDetailsDTO> orderDetails) {}
