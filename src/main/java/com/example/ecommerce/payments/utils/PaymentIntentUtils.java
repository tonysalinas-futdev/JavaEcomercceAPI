package com.example.ecommerce.payments.utils;

import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.payments.dto.CreatePaymentDto;
import com.example.ecommerce.payments.paymentintentwrapper.PaymentIntentWraper;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentIntentUtils {

  private final PaymentIntentWraper wraper;

  private PaymentIntentCreateParams buildPaymentIntentParams(Order order, CreatePaymentDto dto) {
    return PaymentIntentCreateParams.builder()
        .setAmount(order.getTotalAmount().longValue())
        .setCurrency(dto.getCurrency())
        .setAutomaticPaymentMethods(
            PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
        .build();
  }

  public PaymentIntent createPaymentIntent(Order order, CreatePaymentDto dto)
      throws StripeException {
    PaymentIntentCreateParams params = buildPaymentIntentParams(order, dto);
    return wraper.createPaymentIntent(params);
  }
}
