package com.example.ecommerce.categories.service;

import com.example.ecommerce.categories.categoryDTOs.CategoryDetailsDTO;
import com.example.ecommerce.categories.categoryDTOs.CategoryListDTO;
import com.example.ecommerce.categories.categoryDTOs.CreateCategoryDTO;
import com.example.ecommerce.categories.categoryDTOs.UpdateCategoryDTO;
import com.example.ecommerce.categories.mappers.CategoryMappers;
import com.example.ecommerce.categories.model.Category;
import com.example.ecommerce.categories.repository.CategoryRepository;
import com.example.ecommerce.shared.exceptions.AlreadyExistsException;
import com.example.ecommerce.shared.exceptions.InvalidRequestException;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryService {
  private final CategoryRepository repo;
  private final CategoryMappers mapper;

  public Category getEntityByIdOrThrow(Long categoryId) {
    return repo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
  }

  @Transactional
  public CategoryDetailsDTO createCategory(CreateCategoryDTO dto) {
    if (repo.findByName(dto.name()).isPresent()) {
      throw new AlreadyExistsException("This name is already used");
    }
    Category category = mapper.createCategoryDTOtoEntity(dto);

    repo.saveAndFlush(category);
    log.atInfo()
        .addKeyValue("event_type", "category_created")
        .addKeyValue("category_id", category.getId())
        .addKeyValue("category_name", category.getName())
        .log("Successfully created category");
    return mapper.entityToCategoryDetailsDTO(category);
  }

  public List<CategoryListDTO> getAllCategories() {
    List<Category> categories = repo.findAll();

    return categories.stream().map(mapper::entityToCategoryListDTO).toList();
  }

  public CategoryDetailsDTO getCategoryDetailsDTOById(Long categoryId) {

    return mapper.entityToCategoryDetailsDTO(getEntityByIdOrThrow(categoryId));
  }

  @Transactional
  public CategoryDetailsDTO updateCategory(UpdateCategoryDTO dto, Long categoryId) {
    if (dto.getName() != null) {
      validateCategoryNameUniqueness(dto.getName());
    }
    Category category = getEntityByIdOrThrow(categoryId);
    mapper.updateEntity(dto, category);

    repo.saveAndFlush(category);

    log.atInfo()
        .addKeyValue("event_type", "category_updated")
        .addKeyValue("category_id", category.getId())
        .addKeyValue("category_name", category.getName())
        .log("Successfully updated category");

    return mapper.entityToCategoryDetailsDTO(category);
  }

  public void validateCategoryNameUniqueness(String name) {
    if (repo.findByName(name).isPresent()) {
      throw new AlreadyExistsException("This name is already used");
    }
  }

  @Transactional
  public void deleteCategory(Long categoryId) {
    Category category = getEntityByIdOrThrow(categoryId);
    if (!category.getProducts().isEmpty()) {
      throw new InvalidRequestException(
          "You cannot delete this category because it has associated products");
    }
    repo.delete(category);
    log.atInfo()
        .addKeyValue("event_type", "category_deleted")
        .addKeyValue("category_id", categoryId)
        .log("Successfully deleted category");
  }
}
