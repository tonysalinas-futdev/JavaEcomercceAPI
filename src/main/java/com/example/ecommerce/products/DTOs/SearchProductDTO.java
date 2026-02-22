package com.example.ecommerce.products.DTOs;

import jakarta.validation.constraints.AssertTrue;
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
public class SearchProductDTO {
  private String name;

  private Double minPrice;

  private Double maxPrice;

  @AssertTrue(message = "The max price must be bigger than the min price")
  public Boolean validatePrices() {
    if (minPrice == null || maxPrice == null) {
      return true;
    }
    return minPrice <= maxPrice;
  }

  private Long categoryId;

  @Builder.Default private Integer page = 0;

  @Builder.Default private Integer size = 10;
}
