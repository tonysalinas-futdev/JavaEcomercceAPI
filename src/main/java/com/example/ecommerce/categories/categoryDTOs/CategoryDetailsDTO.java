package com.example.ecommerce.categories.categoryDTOs;

import com.example.ecommerce.products.DTOs.ProductListDTO;
import java.util.List;
import lombok.Builder;

@Builder
public record CategoryDetailsDTO(
    Long id, String name, String description, String pic, List<ProductListDTO> products) {}
