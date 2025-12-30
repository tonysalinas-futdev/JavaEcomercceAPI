package com.example.Ecomercce.products.DTOs;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProduct {

  private String name;

  @Size(max = 2000)
  private String description;

  private String pic;

  private Double price;

  @Positive private Integer stock;

  private Boolean available;
}
