package com.example.ecommerce.order.events;

import com.example.ecommerce.order.models.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderCompletedEvent {
  private final Order order;
}
