package com.example.ecommerce.products.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
public record ProductListDTO(
       String name,

        Double price,

        String description,

        String pic
) {

}
