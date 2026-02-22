package com.example.ecommerce.payments.utils;

import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.payments.dto.CreatePaymentDto;
import com.example.ecommerce.payments.model.Payment;
import com.example.ecommerce.payments.status.PaymentStatus;

public class BuildPaymentEntity {

  public static Payment build(CreatePaymentDto dto, Order order, String paymentIntentId) {
    return Payment.builder()
        .orderId(order.getId())
        .amount(order.getTotalAmount())
        .currency(dto.getCurrency())
        .status(PaymentStatus.PENDING)
        .userId(order.getUser().getId())
        .paymentIntentId(paymentIntentId)
        .build();
  }
}
