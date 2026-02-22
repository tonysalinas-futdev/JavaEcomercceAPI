package com.example.ecommerce.order.dtos.order;

import com.example.ecommerce.order.dtos.orderdetails.OrderDetailsDTO;
import com.example.ecommerce.order.models.OrderStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderDTO {
  private Long id;
  private Long userId;
  private Double totalAmount;
  private OrderStatus status;
  private List<OrderDetailsDTO> orderDetails;
}
