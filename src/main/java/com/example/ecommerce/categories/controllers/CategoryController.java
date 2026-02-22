package com.example.ecommerce.categories.controllers;

import com.example.ecommerce.categories.categoryDTOs.CategoryDetailsDTO;
import com.example.ecommerce.categories.categoryDTOs.CategoryListDTO;
import com.example.ecommerce.categories.categoryDTOs.CreateCategoryDTO;
import com.example.ecommerce.categories.categoryDTOs.UpdateCategory;
import com.example.ecommerce.categories.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','MANAGER') and hasAuthority('EDIT_CATALOGUE')")
public class CategoryController {
  CategoryService service;

  @GetMapping("/{id}")
  @PreAuthorize("permitAll()")
  public ResponseEntity<CategoryDetailsDTO> getCategory(@PathVariable @Positive Long id) {

    return ResponseEntity.status(200).body(service.getCategoryDTOById(id));
  }

  @GetMapping
  @PreAuthorize("permitAll()")
  public ResponseEntity<List<CategoryListDTO>> getAllCategories() {

    return ResponseEntity.status(200).body(service.getAllCategories());
  }

  @PostMapping()
  public ResponseEntity<CategoryDetailsDTO> createCategory(
      @RequestBody @Valid CreateCategoryDTO dto) {

    CategoryDetailsDTO category = service.createCategory(dto);
    URI location = URI.create("/api/v1/categories/" + category.getId());

    return ResponseEntity.created(location).body(category);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CategoryDetailsDTO> updateCategory(
      @RequestBody @Valid UpdateCategory dto, @PathVariable @Positive Long id) {

    return ResponseEntity.status(200).body(service.updateCategory(dto, id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCategory(@Positive @PathVariable Long id) {
    service.deleteCategory(id);

    return ResponseEntity.noContent().build();
  }
}
