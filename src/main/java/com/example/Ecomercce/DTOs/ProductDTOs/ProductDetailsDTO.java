package com.example.Ecomercce.DTOs.ProductDTOs;


import com.example.Ecomercce.DTOs.CategoryDTOs.CategoryListDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ProductDetailsDTO {

    private Long id;

    private String name;

    private String description;

    private String pic;

    private Double price;

    private CategoryListDTO category;
    
}
