package com.example.ecommerce.payments.utils;

import com.example.ecommerce.payments.status.PaymentStatus;

public class IdentifyPaymentEvent {
  public static PaymentStatus getEventAndReturnStatus(String event) {
    if (event.equals("payment_intent.succeeded")) {
      return PaymentStatus.PAID;
    } else if (event.equals("payment_intent.canceled")) {
      return PaymentStatus.CANCELLED;

    } else if (event.equals("payment_intent.payment_failed")) {
      return PaymentStatus.FAILED;
    }

    return null;
  }
}
