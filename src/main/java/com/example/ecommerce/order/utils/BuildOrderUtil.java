package com.example.ecommerce.order.utils;

import com.example.ecommerce.cart.models.Cart;
import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.order.models.OrderDetails;
import com.example.ecommerce.order.models.OrderStatus;
import com.example.ecommerce.users.models.User;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BuildOrderUtil {

  public static Order build(UUID requestId, User user, Double totalAmount, Cart cart) {

    Order order =
        Order.builder()
            .requestId(requestId)
            .status(OrderStatus.PENDING)
            .totalAmount(totalAmount)
            .user(user)
            .build();

    List<OrderDetails> details =
        cart.getItems().stream()
            .map(
                itm ->
                    OrderDetails.builder()
                        .product(itm.getProduct())
                        .quantity(itm.getQuantity())
                        .order(order)
                        .build())
            .collect(Collectors.toList());

    order.setOrderDetails(details);
    return order;
  }
}
