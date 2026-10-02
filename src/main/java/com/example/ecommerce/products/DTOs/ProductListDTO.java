package com.example.ecommerce.products.DTOs;

import lombok.Builder;

@Builder
public record ProductListDTO(String name, Double price, String description, String pic) {}
