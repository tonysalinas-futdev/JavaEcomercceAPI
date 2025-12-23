package com.example.Ecomercce.DTOs.CategoryDTOs;

import jakarta.validation.constraints.NotEmpty;

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
public class UpdateCategory {
     
    @NotEmpty
    @Size(max = 300)
    private String name;

    @Size(max = 2000)
    private String description;

    private String pic;
}
