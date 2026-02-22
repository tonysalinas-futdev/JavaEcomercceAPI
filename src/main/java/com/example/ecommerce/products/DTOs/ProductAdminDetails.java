package com.example.ecommerce.products.DTOs;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductAdminDetails extends ProductDetailsDTO {
  private Boolean available;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private Integer stock;
}
