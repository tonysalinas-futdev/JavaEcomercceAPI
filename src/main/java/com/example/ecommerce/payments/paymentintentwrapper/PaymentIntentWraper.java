package com.example.ecommerce.payments.paymentintentwrapper;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class PaymentIntentWraper {

  public PaymentIntent createPaymentIntent(PaymentIntentCreateParams params)
      throws StripeException {
    return PaymentIntent.create(params);
  }
}
