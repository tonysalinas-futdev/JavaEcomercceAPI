package com.example.ecommerce.payments.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
  private StripeProperties stripeProperties;

  public StripeConfig(StripeProperties stripeProperties) {
    this.stripeProperties = stripeProperties;
  }

  @PostConstruct
  public void init() {
    Stripe.apiKey = stripeProperties.getSecretKey();
  }
}
