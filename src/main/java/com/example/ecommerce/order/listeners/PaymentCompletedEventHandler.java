package com.example.ecommerce.order.listeners;

import com.example.ecommerce.order.events.OrderCompletedEvent;
import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.order.models.OrderStatus;
import com.example.ecommerce.order.service.OrderService;
import com.example.ecommerce.payments.events.PaymentCompletedEvent;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCompletedEventHandler {
  private final OrderService service;
  private final ApplicationEventPublisher publisher;

  @Transactional
  @EventListener
  public void handle(PaymentCompletedEvent event) {
      log.atInfo()
              .addKeyValue("event_type", "payment_event_received")
              .addKeyValue("user_id", event.getUserId())
              .addKeyValue("order_id", event.getOrderId())
              .addKeyValue("payment_id",event.getPaymentId())
              .log("Handling payment completed event");

    Order order = service.findEntityByIdAndLoadUserOrThrow(event.getOrderId());
    service.setStatus(event.getOrderId(), OrderStatus.PAID);
    publisher.publishEvent(new OrderCompletedEvent(order));

      log.atInfo()
              .addKeyValue("event_type", "payment_event_handled")
              .addKeyValue("user_id", event.getUserId())
              .addKeyValue("order_id", event.getOrderId())
              .addKeyValue("payment_id",event.getPaymentId())
              .log(" Payment completed event handling successfully");
  }
}
