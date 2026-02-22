package com.example.ecommerce.categories.categoryDTOs;

import jakarta.validation.constraints.NotBlank;
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
public class CreateCategoryDTO {

  @NotBlank(message = "The category name cannot be blank")
  @Size(max = 300, message = "The max size of category name must be 300")
  private String name;

  @Size(max = 2000, message = "The max size of category description must be 2000")
  private String description;

  private String pic;
}
