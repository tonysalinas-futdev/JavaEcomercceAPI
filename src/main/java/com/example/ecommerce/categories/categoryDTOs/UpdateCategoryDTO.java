package com.example.ecommerce.categories.categoryDTOs;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class UpdateCategoryDTO {

  @Size(max = 300)
  private final String name;

  @Size(max = 2000)
  private final String description;

  private final String pic;
}
