package com.example.Ecomercce.order.dtos.order;

import com.example.Ecomercce.order.dtos.orderdetails.OrderDetailsDTO;
import com.example.Ecomercce.order.models.OrderStatus;
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
