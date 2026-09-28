package com.example.ecommerce.categories.categoryDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Builder
public record CreateCategoryDTO(
        @NotBlank(message = "The category name cannot be blank")
        @Size(max = 300, message = "The max size of category name must be 300")
        String name,

        @Size(max = 2000, message = "The max size of category description must be 2000")
        String description,
        String pic
) {


}
