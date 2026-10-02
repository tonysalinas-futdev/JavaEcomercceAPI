package com.example.ecommerce.order.service;

import com.example.ecommerce.cart.models.Cart;
import com.example.ecommerce.cart.services.CartService;
import com.example.ecommerce.cart.utils.CalculatePriceService;
import com.example.ecommerce.order.dtos.order.OrderAllDetailsDTO;
import com.example.ecommerce.order.dtos.order.OrderListDTO;
import com.example.ecommerce.order.mappers.order.OrderMapper;
import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.order.models.OrderStatus;
import com.example.ecommerce.order.repositories.OrderRepository;
import com.example.ecommerce.order.utils.BuildOrderUtil;
import com.example.ecommerce.order.utils.OrderPaginationCriteria;
import com.example.ecommerce.products.services.ProductService;
import com.example.ecommerce.products.utils.ProductValidator;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.shared.utils.BuildPageableUtil;
import com.example.ecommerce.shared.utils.PaginationCriteria;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserQueryService;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
  private final CartService cartService;
  private final OrderRepository repo;
  private final ProductService productService;
  private final UserQueryService userQueryService;
  private final OrderMapper mapper;

  public Order findEntityByIdOrThrow(Long orderId) {
    return repo.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
  }

  public OrderAllDetailsDTO findByIdOrThrowAndReturnDto(Long orderId) {
    Order order = findEntityByIdOrThrow(orderId);
    return mapper.entityToAllDetailsDTO(order);
  }

  public Order findEntityByIdAndLoadUserOrThrow(Long orderId) {
    return repo.findByIdAndLoadUser(orderId)
        .orElseThrow(() -> new NotFoundException("Order not found"));
  }

  @Transactional
  public OrderAllDetailsDTO createOrder(Long cartId, UUID requestId, String userEmail) {
    Cart cart = cartService.findByCartItemIdAndBlockRow(cartId);
    User user = userQueryService.findByEmailOrThrow(userEmail);

    cart.getItems()
        .forEach(
            itm -> {
              var product = productService.findByIdOrThrow(itm.getProduct().getId());
              ProductValidator.validateAvailabilityAndStock(product, itm.getQuantity());
            });

    Double totalAmount = CalculatePriceService.calculateTotalAmount(cart);

    Order order = BuildOrderUtil.build(requestId, user, totalAmount, cart);

    repo.saveAndFlush(order);
    log.info("Successfully created order: {}", order);

    return mapper.entityToAllDetailsDTO(order);
  }

  @Transactional
  public Order setStatus(Long orderId, OrderStatus status) {
    Order order = findEntityByIdOrThrow(orderId);
    order.setStatus(status);
    repo.saveAndFlush(order);
    log.info("Assigned status: {}, to order: {}", status.toString(), order);
    return order;
  }

  public Page<OrderListDTO> findByStatus(
      OrderStatus status, PaginationCriteria<OrderPaginationCriteria> paginationCriteria) {
    Pageable pageable = BuildPageableUtil.build(paginationCriteria);
    return repo.findByStatus(status, pageable).map(mapper::entityToOrderListDTO);
  }

  public Page<OrderListDTO> findAllUserOrders(
      Long userID, PaginationCriteria<OrderPaginationCriteria> paginationCriteria) {
    Pageable pageable = BuildPageableUtil.build(paginationCriteria);
    return repo.findByUserId(userID, pageable).map(mapper::entityToOrderListDTO);
  }
}
