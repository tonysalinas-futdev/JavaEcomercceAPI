package com.example.ecommerce.products.DTOs;

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

  @Size(max = 2000, message = "The max size of description should be minor than 2000")
  private String description;

  private String pic;

  @Positive(message = "The price must be positive")
  private Double price;

  @Positive(message = "The integer must be positive")
  private Integer stock;

  private Boolean available;
}
