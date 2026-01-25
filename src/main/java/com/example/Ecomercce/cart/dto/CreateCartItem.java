package com.example.Ecomercce.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateCartItem {

  @Positive @NotNull private Long productId;

  @Positive @NotNull private String userEmail;

  @Positive @NotNull private int quantity;
}
