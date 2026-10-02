package com.example.ecommerce.products.DTOs;

import com.example.ecommerce.categories.categoryDTOs.CategoryListDTO;
import lombok.Builder;

@Builder
public record ProductDetailsDTO(
    Long id,
    String name,
    String description,
    String pic,
    Double price,
    Boolean isAvailable,
    Integer stock,
    CategoryListDTO category) {}
