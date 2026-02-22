package com.example.ecommerce.payments.controllers;

import com.example.ecommerce.payments.config.StripeProperties;
import com.example.ecommerce.payments.dto.CreatePaymentDto;
import com.example.ecommerce.payments.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
public class PaymentController {
  private final PaymentService service;
  private StripeProperties properties;

  @PostMapping("/intent")
  public ResponseEntity<?> createIntent(@RequestBody CreatePaymentDto dto) throws StripeException {
    PaymentIntent intent = service.createIntentAndSavePayment(dto);
    return ResponseEntity.ok(Map.of("clientSecret", intent.getClientSecret()));
  }

  @PostMapping("/webhook")
  public ResponseEntity<String> handleStripeWebhook(
      @RequestBody String payload, @RequestHeader("Stripe-Signature") String header)
      throws SignatureVerificationException {
    Event event = Webhook.constructEvent(payload, header, properties.getWebhookSecret());
    PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
    service.updatePaymentStatus(intent.getId(), event);
    return ResponseEntity.ok().build();
  }
}
