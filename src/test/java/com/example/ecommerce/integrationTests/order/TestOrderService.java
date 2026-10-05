package com.example.ecommerce.integrationTests.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.ecommerce.cart.services.CartService;
import com.example.ecommerce.integrationTests.globalconftest.GlobalConftest;
import com.example.ecommerce.order.dtos.order.OrderAllDetailsDTO;
import com.example.ecommerce.order.dtos.order.OrderListDTO;
import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.order.models.OrderStatus;
import com.example.ecommerce.order.service.OrderService;
import com.example.ecommerce.order.utils.OrderPaginationCriteria;
import com.example.ecommerce.shared.utils.PaginationCriteria;
import com.example.ecommerce.users.models.User;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class TestOrderService {
  @Autowired private OrderService orderService;
  @Autowired private GlobalConftest globalConftest;
  @Autowired private CartService cartService;

  @Test
  @DisplayName(
      "Debe registrar la orden como pendiente de pago y con un OrderDetails por producto del carrito(3)")
  public void shouldCreateOrderSuccessfully() {
    User user = globalConftest.createUser();
    cartService.addItem(user.getId(), 16, 5);
    cartService.addItem(user.getId(), 17, 5);
    cartService.addItem(user.getId(), 19, 5);

    OrderAllDetailsDTO order = orderService.createOrder(UUID.randomUUID(), user.getEmail());

    assertEquals(3, order.orderDetails().size());
    assertEquals(OrderStatus.PENDING, order.status());
  }

  @Test
  public void shouldSetNewStatus() {
    OrderAllDetailsDTO order = orderService.createOrder(UUID.randomUUID(), "camacelmi@gmail.com");

    Order updateOrder = orderService.setStatus(order.id(), OrderStatus.PAID);

    assertEquals(OrderStatus.PAID, updateOrder.getStatus());
  }

  @Test
  public void shouldGet3PaidOrdersOrderByIdSuccessfully() {
    PaginationCriteria<OrderPaginationCriteria> paginationCriteria =
        new PaginationCriteria<>(0, 3, OrderPaginationCriteria.ID, true);
    Page<OrderListDTO> orders = orderService.findByStatus(OrderStatus.PAID, paginationCriteria);

    Assertions.assertEquals(3, orders.getNumberOfElements());
  }
}
