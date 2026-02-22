package com.example.ecommerce.integrationTests.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.ecommerce.order.dtos.order.OrderDTO;
import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.order.models.OrderStatus;
import com.example.ecommerce.order.service.OrderService;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class testOrderService {
  @Autowired private OrderService orderService;

  @Test
  @DisplayName(
      "Debe registrar la orden como pendiente de pago y con un OrderDetails por producto del carrito(3)")
  public void shouldCreateOrderSucessfully() {
    OrderDTO order = orderService.createOrder(1L, UUID.randomUUID(), "camacelmi@gmail.com");

    assertEquals(3, order.getOrderDetails().size());
    assertTrue(order.getStatus().equals(OrderStatus.PENDING));
  }

  @Test
  public void shouldSetNewStatus() {
    OrderDTO order = orderService.createOrder(1L, UUID.randomUUID(), "camacelmi@gmail.com");

    Order updateOrder = orderService.setStatus(order.getId(), OrderStatus.PAID);

    assertTrue(updateOrder.getStatus().equals(OrderStatus.PAID));
  }
}
