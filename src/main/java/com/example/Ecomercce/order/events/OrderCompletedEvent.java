package com.example.Ecomercce.order.events;

import com.example.Ecomercce.order.models.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderCompletedEvent {
  private final Order order;
}
