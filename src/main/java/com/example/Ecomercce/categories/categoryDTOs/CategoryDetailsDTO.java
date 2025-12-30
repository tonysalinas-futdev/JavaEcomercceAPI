package com.example.Ecomercce.categories.categoryDTOs;

import com.example.Ecomercce.products.DTOs.ProductListDTO;
import java.util.List;
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
public class CategoryDetailsDTO {
  private Long id;

  private String name;

  private String description;

  private String pic;

  private List<ProductListDTO> products;
}
