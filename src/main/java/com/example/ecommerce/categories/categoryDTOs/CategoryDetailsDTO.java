package com.example.ecommerce.categories.categoryDTOs;

import com.example.ecommerce.products.DTOs.ProductListDTO;
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
