package com.example.ecommerce.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItemDTO {

  private Long id;
  private Long productId;
  private Integer quantity;
}
