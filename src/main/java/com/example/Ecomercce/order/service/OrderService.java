package com.example.Ecomercce.order.service;

import com.example.Ecomercce.cart.models.Cart;
import com.example.Ecomercce.cart.services.CartService;
import com.example.Ecomercce.cart.utils.CalculatePriceService;
import com.example.Ecomercce.logging.service.LoggerService;
import com.example.Ecomercce.order.models.Order;
import com.example.Ecomercce.order.models.OrderDetails;
import com.example.Ecomercce.order.models.OrderStatus;
import com.example.Ecomercce.order.repositories.OrderRepository;
import com.example.Ecomercce.products.utils.ValidateProduct;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import com.example.Ecomercce.shared.exceptions.PersistenceErrorException;
import com.example.Ecomercce.shared.utils.PageableUtils;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.services.UserAdminService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
  private final CartService cartService;
  private final OrderRepository repo;
  private final ValidateProduct validator;
  private final UserAdminService userService;
  private final LoggerService logger;

  private Order buildOrder(UUID requestId, User user, Double totalAmount, Cart cart) {

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

  public Order getOrderEntityById(Long orderId) {
    return repo.findById(orderId).orElseThrow(() -> new NotFoundException("Orden no encontrada"));
  }

  public Order getEntityByIdAndLoadUser(Long orderId) {
    return repo.findByIdAndLoadUser(orderId)
        .orElseThrow(() -> new NotFoundException("Orden no encontrada"));
  }

  @Transactional
  public Order createOrder(Long cartId, UUID requestId, String userEmail) {
    Cart cart = cartService.getByCartItemIdAndBlockRow(cartId);

    User user = userService.getUserByEmail(userEmail);

    cart.getItems()
        .forEach(
            itm -> {
              validator.validateAvaibilityAndStockAndReturn(
                  itm.getProduct().getId(), itm.getQuantity());
            });

    Double totalAmount = CalculatePriceService.calculateTotalAmount(cart);

    Order order = buildOrder(requestId, user, totalAmount, cart);
    try {
      repo.saveAndFlush(order);
      logger.createBusinnessEventLog("order_created", "createOrder", "order_id", order.getId());
      return order;
    } catch (DataAccessException e) {
      throw new PersistenceErrorException("Database Error", e);
    }
  }

  @Transactional
  public Order setStatus(Long orderId, OrderStatus status) {
    Order order = getOrderEntityById(orderId);
    order.setStatus(status);
    repo.save(order);
    return order;
  }

  public Page<Order> getByStatus(OrderStatus status, Integer number, Integer size) {
    var verifyNumber = PageableUtils.verifyPage(number);
    var verifySize = PageableUtils.verifySize(size);
    Pageable pageable = PageRequest.of(verifyNumber, verifySize, Sort.by("createdAt").ascending());

    Page<Order> page = repo.findByStatus(status, pageable);

    return page;
  }

  public Page<Order> getAllUserOrders(Long userID, Integer size, Integer number) {
    var verifySize = PageableUtils.verifySize(size);
    var verifyPage = PageableUtils.verifyPage(number);
    userService.getUserEntityById(userID);
    Pageable pageable = PageRequest.of(verifyPage, verifySize, Sort.by("createdAt").ascending());
    return repo.findByUserId(userID, pageable);
  }
}
