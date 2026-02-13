package com.example.Ecomercce.payments.utils;

import com.example.Ecomercce.payments.status.PaymentStatus;

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
