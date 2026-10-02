package com.example.ecommerce.order.listeners;

import com.example.ecommerce.order.events.OrderCompletedEvent;
import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.order.models.OrderStatus;
import com.example.ecommerce.order.service.OrderService;
import com.example.ecommerce.payments.events.PaymentCompletedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class HandlePaymentCompletedEvent {
  private final OrderService service;
  private final ApplicationEventPublisher publisher;

  @EventListener
  public void handle(PaymentCompletedEvent event) {
    log.info("Handle payment completed event: {}", event);
    Order order = service.findEntityByIdAndLoadUserOrThrow(event.getOrderId());
    service.setStatus(event.getOrderId(), OrderStatus.PAID);
    publisher.publishEvent(new OrderCompletedEvent(order));
  }
}
