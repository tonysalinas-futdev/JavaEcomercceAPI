package com.example.Ecomercce.products.DTOs;

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

  @AssertTrue(message = "El precio mínimo no puede ser mayor que el precio máximo")
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
