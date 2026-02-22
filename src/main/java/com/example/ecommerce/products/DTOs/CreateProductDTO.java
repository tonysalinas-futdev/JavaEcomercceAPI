package com.example.ecommerce.products.DTOs;

import jakarta.validation.constraints.NotBlank;
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
public class CreateProductDTO {

  @NotBlank(message = "Product name cannot be blank")
  private String name;

  @Size(max = 2000)
  private String description;

  private String pic;

  private Double price;

  @Positive(message = "Stock must be integer")
  private Integer stock;

  private Boolean available;

  @Positive(message = "Category id must be positive")
  private Long categoryId;
}
