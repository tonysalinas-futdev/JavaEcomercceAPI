package com.example.ecommerce.order.utils;

import com.example.ecommerce.cart.models.Cart;
import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.order.models.OrderDetails;
import com.example.ecommerce.order.models.OrderStatus;
import com.example.ecommerce.users.models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildOrderUtil {

  public static Order buildOrder(UUID requestId, User user, Double totalAmount, Cart cart) {

    Order order =
        Order.builder()
            .requestId(requestId)
            .status(OrderStatus.PENDING)
            .totalAmount(totalAmount)
            .user(user)
            .build();

    List<OrderDetails> details = new ArrayList<>();
    cart.getItems()
        .forEach(
            itm -> {
              OrderDetails detail =
                  OrderDetails.builder()
                      .product(itm.getProduct())
                      .quantity(itm.getQuantity())
                      .order(order)
                      .build();

              details.add(detail);
            });

    order.setOrderDetails(details);
    return order;
  }
}
