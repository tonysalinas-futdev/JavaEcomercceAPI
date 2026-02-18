package com.example.Ecomercce.integrationTests.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.Ecomercce.order.dtos.order.OrderDTO;
import com.example.Ecomercce.order.models.Order;
import com.example.Ecomercce.order.models.OrderStatus;
import com.example.Ecomercce.order.service.OrderService;
import com.example.Ecomercce.payments.dto.CreatePaymentDto;
import com.example.Ecomercce.payments.model.Payment;
import com.example.Ecomercce.payments.paymentintentwrapper.PaymentIntentWraper;
import com.example.Ecomercce.payments.service.PaymentService;
import com.example.Ecomercce.payments.status.PaymentStatus;
import com.example.Ecomercce.products.model.Product;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class TestPaymentCompletedEvent {
  @Autowired private OrderService orderService;
  @Autowired private PaymentService paymentService;

  @MockitoBean private PaymentIntentWraper wraper;

  public CreatePaymentDto createOrderAndReturnCreatePaymentDto() {

    OrderDTO order = orderService.createOrder(1L, UUID.randomUUID(), "camacelmi@gmail.com");

    return CreatePaymentDto.builder().currency("usd").orderId(order.getId()).build();
  }

  @BeforeEach
  private void mockPaymentIntentWraper() throws StripeException {

    PaymentIntent fakePaymentIntent = new PaymentIntent();
    fakePaymentIntent.setId("id_43");
    fakePaymentIntent.setCurrency("usd");

    when(wraper.createPaymentIntent(any(PaymentIntentCreateParams.class)))
        .thenReturn(fakePaymentIntent);
  }

  private void savePaymentEntity() throws StripeException {
    CreatePaymentDto dto = createOrderAndReturnCreatePaymentDto();
    paymentService.createIntentAndSavePayment(dto);
  }

  private Event createFakeEvent() {
    Event event = new Event();
    event.setId("453");
    event.setType("payment_intent.succeeded");
    return event;
  }

  @Test
  public void shouldSetOrderAsPaidAndUpdateProductsStock() throws StripeException {
    savePaymentEntity();
    paymentService.updatePaymentStatus("id_43", createFakeEvent());
    Payment payment = paymentService.getByOrderId(1L);
    Order order = orderService.getOrderEntityById(1L);
    List<Product> products = order.getOrderDetails().stream().map(itm -> itm.getProduct()).toList();

    Product productWithId16AndInitialStock18 =
        products.stream().filter(pro -> pro.getId().equals(16L)).findFirst().get();
    Product productWithId17AndInitialStock40 =
        products.stream().filter(pro -> pro.getId().equals(17L)).findFirst().get();
    Product productWithId19AndInitialStock18 =
        products.stream().filter(pro -> pro.getId().equals(19L)).findFirst().get();

    assertTrue(payment.getStatus().equals(PaymentStatus.PAID));
    assertTrue(order.getStatus().equals(OrderStatus.PAID));
    assertThat(productWithId16AndInitialStock18.getStock()).isEqualTo(16); // Expected 16
    assertThat(productWithId17AndInitialStock40.getStock()).isEqualTo(36); // Expected 36
    assertThat(productWithId19AndInitialStock18.getStock()).isEqualTo(13); // Expected 13
  }
}
