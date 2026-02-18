package com.example.Ecomercce.cart.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartDetailsDTO {
  private Long id;
  private List<CartItemDTO> items;
  private Long userId;
}
