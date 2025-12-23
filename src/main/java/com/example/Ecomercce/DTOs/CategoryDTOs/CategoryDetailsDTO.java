package com.example.Ecomercce.DTOs.CategoryDTOs;

import java.util.List;

import com.example.Ecomercce.DTOs.ProductDTOs.ProductListDTO;

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
