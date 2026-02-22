package com.example.ecommerce.payments.service;

import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.order.service.OrderService;
import com.example.ecommerce.payments.dto.CreatePaymentDto;
import com.example.ecommerce.payments.events.PaymentCompletedEvent;
import com.example.ecommerce.payments.model.Payment;
import com.example.ecommerce.payments.model.WebHookEvent;
import com.example.ecommerce.payments.repository.PaymentRepository;
import com.example.ecommerce.payments.status.PaymentStatus;
import com.example.ecommerce.payments.utils.BuildPaymentEntity;
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

  private Payment findByPaymentIntentOrThrow(String paymentIntentId) {
    return repo.findByPaymentIntentId(paymentIntentId)
        .orElseThrow(() -> new NotFoundException("Payment not found"));
  }

  public Payment findByOrderIdOrThrow(Long orderId) {
    return repo.findByOrderId(orderId)
        .orElseThrow(() -> new NotFoundException("Payment not found"));
  }

  public PaymentIntent createIntentAndSavePayment(CreatePaymentDto dto) throws StripeException {
    Order order = orderService.findEntityByIdAndLoadUserOrThrow(dto.getOrderId());

    PaymentIntent intent = utils.createPaymentIntent(order, dto);

    Payment payment = BuildPaymentEntity.build(dto, order, intent.getId());
    repo.save(payment);

    return intent;
  }

  public Payment setStatus(String paymentIntentId, PaymentStatus status) {
    Payment payment = findByPaymentIntentOrThrow(paymentIntentId);

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
