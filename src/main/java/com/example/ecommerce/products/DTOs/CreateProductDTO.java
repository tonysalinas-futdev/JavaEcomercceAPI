package com.example.ecommerce.products.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateProductDTO(
    @NotBlank(message = "Product name cannot be blank") String name,
    @Size(max = 2000, message = "Description cannot exceed 2000 characters") String description,
    String pic,
    @Positive(message = "Price must be positive") Double price,
    @Positive(message = "Stock must be a positive integer") Integer stock,
    Boolean isAvailable,
    @Positive(message = "Category id must be positive") Long categoryId) {}
