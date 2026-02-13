package com.example.Ecomercce.integrationTests.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.Ecomercce.order.models.Order;
import com.example.Ecomercce.order.models.OrderStatus;
import com.example.Ecomercce.order.service.OrderService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class testOrderService {
  @Autowired private OrderService orderService;

  @Test
  @DisplayName(
      "Debe registrar la orden como pendiente de pago y con un OrderDetails por producto del carrito(3)")
  public void shouldCreateOrderSucessfully() {
    Order order = orderService.createOrder(1L, UUID.randomUUID(), "camacelmi@gmail.com");

    assertEquals(3, order.getOrderDetails().size());
    assertTrue(order.getStatus().equals(OrderStatus.PENDING));
  }

  @Test
  public void shouldSetNewStatus() {
    Order order = orderService.createOrder(1L, UUID.randomUUID(), "camacelmi@gmail.com");

    Order updateOrder = orderService.setStatus(order.getId(), OrderStatus.PAID);

    assertTrue(updateOrder.getStatus().equals(OrderStatus.PAID));
  }
}
