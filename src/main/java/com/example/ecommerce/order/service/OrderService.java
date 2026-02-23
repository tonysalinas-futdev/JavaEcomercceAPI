package com.example.ecommerce.order.service;

import com.example.ecommerce.cart.models.Cart;
import com.example.ecommerce.cart.services.CartService;
import com.example.ecommerce.cart.utils.CalculatePriceService;
import com.example.ecommerce.logger.annotations.LogOrderEvent;
import com.example.ecommerce.order.dtos.order.OrderDTO;
import com.example.ecommerce.order.logs.events.OrderDetailsLogEvents;
import com.example.ecommerce.order.logs.events.OrderLogsEvents;
import com.example.ecommerce.order.mappers.order.OrderMapper;
import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.order.models.OrderStatus;
import com.example.ecommerce.order.repositories.OrderRepository;
import com.example.ecommerce.order.utils.BuildOrderUtil;
import com.example.ecommerce.products.services.ProductService;
import com.example.ecommerce.products.utils.ProductValidator;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.shared.exceptions.PersistenceErrorException;
import com.example.ecommerce.shared.utils.PageableUtils;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserQueryService;
import jakarta.transaction.Transactional;
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
  private final ProductService productService;
  private final UserQueryService userQueryService;
  private final OrderMapper mapper;

  public Order findEntityByIdOrThrow(Long orderId) {
    return repo.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
  }

  public OrderDTO findByIdAndReturnDto(Long orderId) {
    Order order = findEntityByIdOrThrow(orderId);
    return mapper.entityToOrderDTO(order);
  }

  public Order findEntityByIdAndLoadUserOrThrow(Long orderId) {
    return repo.findByIdAndLoadUser(orderId)
        .orElseThrow(() -> new NotFoundException("Order not found"));
  }

  @Transactional
  @LogOrderEvent(
      class_ = OrderService.class,
      orderEvent = OrderLogsEvents.ORDER_CREATED,
      detailsEvent = OrderDetailsLogEvents.ORDER_DETAILS_CREATED)
  public OrderDTO createOrder(Long cartId, UUID requestId, String userEmail) {
    Cart cart = cartService.findByCartItemIdAndBlockRow(cartId);

    User user = userQueryService.findByEmailOrThrow(userEmail);

    cart.getItems()
        .forEach(
            itm -> {
              var product = productService.findByIdOrThrow(itm.getProduct().getId());
              ProductValidator.validateAvaibilityAndStockAndReturn(product, itm.getQuantity());
            });

    Double totalAmount = CalculatePriceService.calculateTotalAmount(cart);

    Order order = BuildOrderUtil.buildOrder(requestId, user, totalAmount, cart);
    try {
      repo.saveAndFlush(order);
      Order refreshedOrder = findEntityByIdOrThrow(order.getId());
      return mapper.entityToOrderDTO(refreshedOrder);
    } catch (DataAccessException e) {
      throw new PersistenceErrorException("Database Error", e);
    }
  }

  @Transactional
  @LogOrderEvent(orderEvent = OrderLogsEvents.ORDER_UPDATED, class_ = OrderService.class)
  public Order setStatus(Long orderId, OrderStatus status) {
    Order order = findEntityByIdOrThrow(orderId);
    order.setStatus(status);
    repo.save(order);
    return order;
  }

  public Page<Order> findByStatus(OrderStatus status, Integer number, Integer size) {
    var verifyNumber = PageableUtils.verifyPage(number);
    var verifySize = PageableUtils.verifySize(size);
    Pageable pageable = PageRequest.of(verifyNumber, verifySize, Sort.by("createdAt").ascending());

    Page<Order> page = repo.findByStatus(status, pageable);

    return page;
  }

  public Page<Order> findAllUserOrders(Long userID, Integer size, Integer number) {
    var verifySize = PageableUtils.verifySize(size);
    var verifyPage = PageableUtils.verifyPage(number);
    userQueryService.findEntityByIdOrThrow(userID);
    Pageable pageable = PageRequest.of(verifyPage, verifySize, Sort.by("createdAt").ascending());
    return repo.findByUserId(userID, pageable);
  }
}
