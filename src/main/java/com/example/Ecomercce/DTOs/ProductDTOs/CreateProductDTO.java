package com.example.Ecomercce.DTOs.ProductDTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class CreateProductDTO {
    
    @NotNull
    @NotEmpty
    private String name;

    @Size(max = 2000)
    private String description;

    private String pic;


    private Double price;

    @Positive
    private Integer stock;

    private Boolean available;

    @Positive
    private Long categoryId;

    }


