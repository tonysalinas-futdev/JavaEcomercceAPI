package com.example.Ecomercce.order.listeners;

import com.example.Ecomercce.order.events.OrderCompletedEvent;
import com.example.Ecomercce.order.models.Order;
import com.example.Ecomercce.order.models.OrderStatus;
import com.example.Ecomercce.order.service.OrderService;
import com.example.Ecomercce.payments.events.PaymentCompletedEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HandlePaymentCompletedEvent {
  private final OrderService service;
  private final ApplicationEventPublisher publisher;

  @EventListener
  public void setCompletedStatus(PaymentCompletedEvent event) {
    Order order = service.getEntityByIdAndLoadUser(event.getOrderId());
    service.setStatus(event.getOrderId(), OrderStatus.PAID);
    publisher.publishEvent(new OrderCompletedEvent(order));
  }
}
