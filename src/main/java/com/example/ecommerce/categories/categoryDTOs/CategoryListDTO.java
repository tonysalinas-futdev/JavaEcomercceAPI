package com.example.ecommerce.categories.categoryDTOs;

import lombok.*;


@Builder
public record CategoryListDTO(
        Long id,

        String name,

        String description,

        String pic
) {

}
