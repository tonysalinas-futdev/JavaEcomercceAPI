package com.example.Ecomercce.DTOs.ProductDTOs;

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
public class ProductListDTO {
    private String name;

    private Double price;

    private String description;

    private String pic;


}
