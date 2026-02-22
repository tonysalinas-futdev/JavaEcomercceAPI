package com.example.ecommerce.payments.service;

import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.order.service.OrderService;
import com.example.ecommerce.payments.dto.CreatePaymentDto;
import com.example.ecommerce.payments.events.PaymentCompletedEvent;
import com.example.ecommerce.payments.model.Payment;
import com.example.ecommerce.payments.model.WebHookEvent;
import com.example.ecommerce.payments.repository.PaymentRepository;
import com.example.ecommerce.payments.status.PaymentStatus;
import com.example.ecommerce.payments.utils.IdentifyPaymentEvent;
import com.example.ecommerce.payments.utils.PaymentIntentUtils;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {
  private OrderService orderService;
  private PaymentRepository repo;
  private WebhookService webhookService;
  private PaymentIntentUtils utils;
  private final ApplicationEventPublisher publisher;

  private Payment getByPaymentIntentId(String paymentIntentId) {
    return repo.findByPaymentIntentId(paymentIntentId)
        .orElseThrow(() -> new NotFoundException("Payment not found"));
  }

  public Payment getByOrderId(Long orderId) {
    return repo.findByOrderId(orderId)
        .orElseThrow(() -> new NotFoundException("Payment not found"));
  }

  public void buildAndSavePayment(CreatePaymentDto dto, Order order, String paymentIntentId) {
    Payment entity =
        Payment.builder()
            .orderId(order.getId())
            .amount(order.getTotalAmount())
            .currency(dto.getCurrency())
            .status(PaymentStatus.PENDING)
            .userId(order.getUser().getId())
            .paymentIntentId(paymentIntentId)
            .build();

    repo.save(entity);
  }

  public PaymentIntent createIntentAndSavePayment(CreatePaymentDto dto) throws StripeException {
    Order order = orderService.getEntityByIdAndLoadUser(dto.getOrderId());

    PaymentIntent intent = utils.createPaymentIntent(order, dto);

    buildAndSavePayment(dto, order, intent.getId());

    return intent;
  }

  public Payment setStatus(String paymentIntentId, PaymentStatus status) {
    Payment payment = getByPaymentIntentId(paymentIntentId);

    payment.setStatus(status);
    repo.saveAndFlush(payment);
    return payment;
  }

  public void updatePaymentStatus(String paymentIntentId, Event event) {
    Optional<WebHookEvent> existingEvent = webhookService.getByEventId(event.getId());
    if (existingEvent.isPresent()) {
      return;
    }
    PaymentStatus status = IdentifyPaymentEvent.getEventAndReturnStatus(event.getType());
    Payment payment = setStatus(paymentIntentId, status);
    if (status.equals(PaymentStatus.PAID)) {
      publisher.publishEvent(
          new PaymentCompletedEvent(payment.getId(), payment.getOrderId(), payment.getUserId()));
    }
  }
}
