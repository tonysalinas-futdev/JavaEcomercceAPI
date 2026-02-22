package com.example.ecommerce.categories.service;

import com.example.ecommerce.categories.categoryDTOs.CategoryDetailsDTO;
import com.example.ecommerce.categories.categoryDTOs.CategoryListDTO;
import com.example.ecommerce.categories.categoryDTOs.CreateCategoryDTO;
import com.example.ecommerce.categories.categoryDTOs.UpdateCategory;
import com.example.ecommerce.categories.logs.events.CategoryLogsEvents;
import com.example.ecommerce.categories.mappers.CategoryMappers;
import com.example.ecommerce.categories.model.Category;
import com.example.ecommerce.categories.repository.CategoryRepository;
import com.example.ecommerce.logger.annotations.LogCategoryEvent;
import com.example.ecommerce.logger.annotations.LogDeleteEntityEvent;
import com.example.ecommerce.shared.exceptions.AlreadyExistsException;
import com.example.ecommerce.shared.exceptions.InvalidRequestException;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.shared.exceptions.PersistenceErrorException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {
  private final CategoryRepository repo;
  private final CategoryMappers mapper;

  public Category getCategoryEntityById(Long categoryId) {

    return repo.findById(categoryId)
        .orElseThrow(() -> new NotFoundException("No se ha encontrado esa categoría"));
  }

  @Transactional
  @LogCategoryEvent(event = CategoryLogsEvents.CATEGORY_CREATED, loggerName = CategoryService.class)
  public CategoryDetailsDTO createCategory(CreateCategoryDTO dto) {
    if (repo.getByName(dto.getName()).isPresent()) {
      throw new AlreadyExistsException("Ya existe una categoría con ese nombre");
    }
    Category category = mapper.createCategoryDTOtoEntity(dto);
    try {
      repo.save(category);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }

    return mapper.entityToCategoryDetailsDTO(category);
  }

  public List<CategoryListDTO> getAllCategories() {
    List<Category> categories = repo.findAll();

    return categories.stream().map(c -> mapper.entityToCategoryListDTO(c)).toList();
  }

  public CategoryDetailsDTO getCategoryDTOById(Long categoryId) {
    Category category = getCategoryEntityById(categoryId);
    return mapper.entityToCategoryDetailsDTO(category);
  }

  @Transactional
  @LogCategoryEvent(event = CategoryLogsEvents.CATEGORY_CREATED, loggerName = CategoryService.class)
  public CategoryDetailsDTO updateCategory(UpdateCategory dto, Long categoryId) {
    Category category = getCategoryEntityById(categoryId);
    mapper.updateEntity(dto, category);
    try {
      repo.save(category);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Database error", ex);
    }

    return mapper.entityToCategoryDetailsDTO(category);
  }

  @Transactional
  @LogDeleteEntityEvent(event = "CATEGORY_DELETED", loggerName = CategoryService.class)
  public void deleteCategory(Long categoryId) {
    Category category = getCategoryEntityById(categoryId);
    if (!category.getProducts().isEmpty()) {
      throw new InvalidRequestException(
          "No puedes eliminar esta categoría porque tiene productos asociaods");
    }

    try {
      repo.delete(category);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }
  }
}
