package com.example.ecommerce.payments.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentCompletedEvent {
  private Long paymentId;
  private Long orderId;
  private Long userId;
}
