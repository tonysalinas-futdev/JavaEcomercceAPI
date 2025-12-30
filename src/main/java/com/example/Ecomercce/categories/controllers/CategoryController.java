package com.example.Ecomercce.categories.controllers;

import com.example.Ecomercce.categories.categoryDTOs.CategoryDetailsDTO;
import com.example.Ecomercce.categories.categoryDTOs.CategoryListDTO;
import com.example.Ecomercce.categories.categoryDTOs.CreateCategoryDTO;
import com.example.Ecomercce.categories.categoryDTOs.UpdateCategory;
import com.example.Ecomercce.categories.service.CategoryService;
import com.example.Ecomercce.shared.exceptions.AlreadyExistsException;
import com.example.Ecomercce.shared.exceptions.DatabaseErrorException;
import com.example.Ecomercce.shared.exceptions.InvalidRequestException;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class CategoryController {
  @Autowired CategoryService service;

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDetailsDTO> getCategory(@PathVariable @Positive Long id)
      throws NotFoundException {
    return ResponseEntity.status(200).body(service.getCategoryDTOById(id));
  }

  @GetMapping
  public ResponseEntity<List<CategoryListDTO>> getAllCategories() {
    return ResponseEntity.status(200).body(service.getAllCategories());
  }

  @PostMapping()
  public ResponseEntity<CategoryDetailsDTO> createCategory(
      @RequestBody @Valid CreateCategoryDTO dto)
      throws AlreadyExistsException, DatabaseErrorException {
        CategoryDetailsDTO category=service.createCategory(dto);
        URI location=URI.create("/api/v1/categories/"+category.getId());
    return ResponseEntity.created(location).body(category);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CategoryDetailsDTO> updateCategory(
      @RequestBody @Valid UpdateCategory dto, @PathVariable @Positive Long id)
      throws NotFoundException, DatabaseErrorException {
    return ResponseEntity.status(200).body(service.updateCategory(dto, id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCategory(@Positive @PathVariable Long id)
      throws NotFoundException, InvalidRequestException, DatabaseErrorException {
    service.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}
