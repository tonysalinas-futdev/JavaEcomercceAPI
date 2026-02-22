package com.example.ecommerce.order.dtos.orderdetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderDetailsDTO {
  private Long id;
  private Long productId;
  private Integer quantity;
  private Long orderId;
}
