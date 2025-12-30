package com.example.Ecomercce.categories.service;

import com.example.Ecomercce.categories.categoryDTOs.CategoryDetailsDTO;
import com.example.Ecomercce.categories.categoryDTOs.CategoryListDTO;
import com.example.Ecomercce.categories.categoryDTOs.CreateCategoryDTO;
import com.example.Ecomercce.categories.categoryDTOs.UpdateCategory;
import com.example.Ecomercce.categories.mappers.CategoryMappers;
import com.example.Ecomercce.categories.model.Category;
import com.example.Ecomercce.categories.repository.CategoryRepository;
import com.example.Ecomercce.shared.exceptions.AlreadyExistsException;
import com.example.Ecomercce.shared.exceptions.DatabaseErrorException;
import com.example.Ecomercce.shared.exceptions.InvalidRequestException;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {
  private final CategoryRepository repo;
  private final CategoryMappers mapper;
  private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

  public Category getCategoryEntityById(Long categoryId) throws NotFoundException {

    return repo.findById(categoryId)
        .orElseThrow(() -> new NotFoundException("No se ha encontrado esa categoría"));
  }

  @Transactional
  public CategoryDetailsDTO createCategory(CreateCategoryDTO dto)
      throws AlreadyExistsException, DatabaseErrorException {
    if (repo.getByName(dto.getName()).isPresent()) {
      throw new AlreadyExistsException("Ya existe una categoría con ese nombre");
    }
    Category category = mapper.createCategoryDTOtoEntity(dto);
    try {
      repo.save(category);
      logger.info(
          "Se ha registrado la categoría con nombre={} y id={} exitosamente",
          category.getName(),
          category.getId());
    } catch (PersistenceException ex) {
      logger.error(
          "No se ha podido registrar la categoría con nombre={}, Error: {}",
          category.getName(),
          ex.getMessage());
      throw new DatabaseErrorException("Error en la base de datos");
    }

    return mapper.entityToCategoryDetailsDTO(category);
  }

  public List<CategoryListDTO> getAllCategories() {
    List<Category> categories = repo.findAll();

    return categories.stream().map(c -> mapper.entityToCategoryListDTO(c)).toList();
  }

  public CategoryDetailsDTO getCategoryDTOById(Long categoryId) throws NotFoundException {
    Category category = getCategoryEntityById(categoryId);
    return mapper.entityToCategoryDetailsDTO(category);
  }

  @Transactional
  public CategoryDetailsDTO updateCategory(UpdateCategory dto, Long categoryId)
      throws NotFoundException, DatabaseErrorException {
    Category category = getCategoryEntityById(categoryId);
    mapper.updateEntity(dto, category);
    try {
      repo.save(category);
      logger.info("Se ha actualizado el producto con id={}", category.getId());
    } catch (PersistenceException ex) {
      logger.error(
          "No se ha podido actualizar la categoría con id={}, Error: {}",
          category.getId(),
          ex.getMessage());
      throw new DatabaseErrorException("Ha ocurrido un error en la base de datos");
    }

    return mapper.entityToCategoryDetailsDTO(category);
  }

  @Transactional
  public void deleteCategory(Long categoryId)
      throws NotFoundException, InvalidRequestException, DatabaseErrorException {
    Category category = getCategoryEntityById(categoryId);
    if (!category.getProducts().isEmpty()) {
      throw new InvalidRequestException(
          "No puedes eliminar esta categoría porque tiene productos asociaods");
    }

    try {
      repo.delete(category);
      logger.info("Eliminado exitosamente la categoría  con id={}", category.getId());

    } catch (PersistenceException ex) {
      logger.error("Fallo al eliminar la categoría con id={}", category.getId());
      throw new DatabaseErrorException("Error en la base de datos");
    }
  }
}
