package com.example.ecommerce.order.listeners;

import com.example.ecommerce.order.events.OrderCompletedEvent;
import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.order.models.OrderStatus;
import com.example.ecommerce.order.service.OrderService;
import com.example.ecommerce.payments.events.PaymentCompletedEvent;
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
