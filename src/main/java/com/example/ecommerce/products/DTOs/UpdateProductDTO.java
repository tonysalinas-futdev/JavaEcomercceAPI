package com.example.ecommerce.products.DTOs;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateProductDTO(
        String name,

        @Size(max = 2000, message = "The max size of description should be minor than 2000")
        String description,

        String pic,

        @Positive(message = "The price must be positive")
        Double price,

        Boolean isAvailable
){}