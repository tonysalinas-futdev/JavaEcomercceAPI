package com.example.Ecomercce.DTOs.CategoryDTOs;

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
public class CategoryListDTO {
    private Long id;
    
    private String name;

    private String description;

    private String pic;
}
