package com.example.ecommerce.products.DTOs;


import java.time.LocalDateTime;
import com.example.ecommerce.categories.categoryDTOs.CategoryListDTO;


public record ProductAdminDetailsDTO(
        Long id,
        String name,
        String description,
        String pic,
        Double price,
        CategoryListDTO category,
        Boolean isAvailable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer stock
) {}
