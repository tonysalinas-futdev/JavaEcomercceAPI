package com.example.Ecomercce.integrationTests.products;

import com.example.Ecomercce.categories.categoryDTOs.CategoryDetailsDTO;
import com.example.Ecomercce.categories.categoryDTOs.CreateCategoryDTO;
import com.example.Ecomercce.categories.service.CategoryService;
import com.example.Ecomercce.products.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductControllersConftest {
  private ProductService productService;
  private CategoryService categoryService;

  public CategoryDetailsDTO returnCategory() {
    CreateCategoryDTO category =
        CreateCategoryDTO.builder()
            .name("CategoriaPrueba")
            .description("Categoría de prueba")
            .build();

    return categoryService.createCategory(category);
  }
}
