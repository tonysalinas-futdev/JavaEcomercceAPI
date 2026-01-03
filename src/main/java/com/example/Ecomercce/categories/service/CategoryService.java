package com.example.Ecomercce.categories.service;

import com.example.Ecomercce.categories.categoryDTOs.CategoryDetailsDTO;
import com.example.Ecomercce.categories.categoryDTOs.CategoryListDTO;
import com.example.Ecomercce.categories.categoryDTOs.CreateCategoryDTO;
import com.example.Ecomercce.categories.categoryDTOs.UpdateCategory;
import com.example.Ecomercce.categories.mappers.CategoryMappers;
import com.example.Ecomercce.categories.model.Category;
import com.example.Ecomercce.categories.repository.CategoryRepository;
import com.example.Ecomercce.logging.service.LoggerService;
import com.example.Ecomercce.shared.exceptions.AlreadyExistsException;
import com.example.Ecomercce.shared.exceptions.InvalidRequestException;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import com.example.Ecomercce.shared.exceptions.PersistenceErrorException;
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
  private final LoggerService logger;

  public Category getCategoryEntityById(Long categoryId) {

    return repo.findById(categoryId)
        .orElseThrow(() -> new NotFoundException("No se ha encontrado esa categoría"));
  }

  @Transactional
  public CategoryDetailsDTO createCategory(CreateCategoryDTO dto) {
    if (repo.getByName(dto.getName()).isPresent()) {
      throw new AlreadyExistsException("Ya existe una categoría con ese nombre");
    }
    Category category = mapper.createCategoryDTOtoEntity(dto);
    try {
      repo.save(category);
      logger.createBusinnessEventLog(
          "category_created", "createCategory", "category_id", category.getId());
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
  public CategoryDetailsDTO updateCategory(UpdateCategory dto, Long categoryId) {
    Category category = getCategoryEntityById(categoryId);
    mapper.updateEntity(dto, category);
    try {
      repo.save(category);
      logger.createBusinnessEventLog(
          "updated_category", "updateCategory", "category_id", categoryId);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Ha ocurrido un error en la base de datos", ex);
    }

    return mapper.entityToCategoryDetailsDTO(category);
  }

  @Transactional
  public void deleteCategory(Long categoryId) {
    Category category = getCategoryEntityById(categoryId);
    if (!category.getProducts().isEmpty()) {
      throw new InvalidRequestException(
          "No puedes eliminar esta categoría porque tiene productos asociaods");
    }

    try {
      repo.delete(category);
      logger.createBusinnessEventLog(
          "deleted_category", "delete_category", "category_id", categoryId);

    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }
  }
}
