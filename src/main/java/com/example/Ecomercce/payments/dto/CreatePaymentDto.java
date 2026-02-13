package com.example.Ecomercce.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreatePaymentDto {
  private Long orderId;
  private String currency;
}
