package com.example.ecommerce.products.DTOs;

import com.example.ecommerce.categories.categoryDTOs.CategoryListDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ProductDetailsDTO {

  private Long id;

  private String name;

  private String description;

  private String pic;

  private Double price;

  private Boolean available;

  private Integer stock;

  private CategoryListDTO category;
}
