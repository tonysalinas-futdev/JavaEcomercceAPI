package com.example.Ecomercce.integrationTests.payment;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.Ecomercce.order.models.Order;
import com.example.Ecomercce.order.service.OrderService;
import com.example.Ecomercce.payments.dto.CreatePaymentDto;
import com.example.Ecomercce.payments.model.Payment;
import com.example.Ecomercce.payments.paymentintentwrapper.PaymentIntentWraper;
import com.example.Ecomercce.payments.service.PaymentService;
import com.example.Ecomercce.payments.status.PaymentStatus;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TestPaymentSevice {
  @Autowired private PaymentService service;
  @Autowired private OrderService orderService;

  @MockitoBean private PaymentIntentWraper wraper;

  CreatePaymentDto paymentDto;

  @BeforeEach
  public void createOrderAndReturnCreatePaymentDto() {

    Order order = orderService.createOrder(1L, UUID.randomUUID(), "camacelmi@gmail.com");

    paymentDto = CreatePaymentDto.builder().currency("usd").orderId(order.getId()).build();
  }

  @BeforeEach
  public void mockPaymentIntent() throws StripeException {

    PaymentIntent fakePaymentIntent = new PaymentIntent();
    fakePaymentIntent.setId("id_43");
    fakePaymentIntent.setCurrency("usd");

    when(wraper.createPaymentIntent(any(PaymentIntentCreateParams.class)))
        .thenReturn(fakePaymentIntent);
  }

  @Test
  @DisplayName("Should save payment entity")
  public void shouldSavePaymentEntity() throws StripeException {

    PaymentIntent intent = service.createIntentAndSavePayment(paymentDto);
    Payment payment = service.getByOrderId(1L);

    assertThat(payment.getPaymentIntentId()).isEqualTo(intent.getId());
    assertThat(payment.getCurrency()).isEqualTo("usd");
    assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
  }

  @Test
  @DisplayName("Should set PAID PaymentStatus")
  public void shouldSetPaidPaymentStatus() throws StripeException {
    PaymentIntent intent = service.createIntentAndSavePayment(paymentDto);
    Payment payment = service.getByOrderId(1L);
    Event event = new Event();
    event.setId("453");
    event.setType("payment_intent.succeeded");

    service.updatePaymentStatus(intent.getId(), event);
    Payment updatePayment = service.getByOrderId(1L);

    assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
    assertThat(updatePayment.getStatus()).isEqualTo(PaymentStatus.PAID);
  }

  @Test
  @DisplayName("Should set FAILED PaymentStatus")
  public void shouldSetFailedPaymentStatus() throws StripeException {
    PaymentIntent intent = service.createIntentAndSavePayment(paymentDto);
    Payment payment = service.getByOrderId(1L);
    Event event = new Event();
    event.setId("453");
    event.setType("payment_intent.payment_failed");

    service.updatePaymentStatus(intent.getId(), event);
    Payment updatePayment = service.getByOrderId(1L);

    assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
    assertThat(updatePayment.getStatus()).isEqualTo(PaymentStatus.FAILED);
  }
}
