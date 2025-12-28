package com.example.Ecomercce.Services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.Ecomercce.DTOs.CategoryDTOs.CategoryDetailsDTO;
import com.example.Ecomercce.DTOs.CategoryDTOs.CategoryListDTO;
import com.example.Ecomercce.DTOs.CategoryDTOs.CreateCategoryDTO;
import com.example.Ecomercce.DTOs.CategoryDTOs.UpdateCategory;
import com.example.Ecomercce.Exceptions.AlreadyExistsException;
import com.example.Ecomercce.Exceptions.DatabaseErrorException;
import com.example.Ecomercce.Exceptions.InvalidRequestException;
import com.example.Ecomercce.Exceptions.NotFoundException;
import com.example.Ecomercce.Mappers.CategoryMappers;
import com.example.Ecomercce.Repositories.CategoryRepository;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Ecomercce.Models.Category;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryRepository repo;
    private CategoryMappers mapper;
    private static final Logger logger=LoggerFactory.getLogger(CategoryService.class);



    private Category getCategoryEntityById(Long categoryId) throws NotFoundException{
        Category category=repo.findById(categoryId).orElseThrow(()-> new NotFoundException("No se ha encontrado esa categoría"));
        return category;
    }


    @Transactional
    public CategoryDetailsDTO createCategory(CreateCategoryDTO dto)throws AlreadyExistsException, DatabaseErrorException{
        if (repo.getByName(dto.getName()).isPresent()) {
            throw new AlreadyExistsException("Ya existe una categoría con ese nombre");
        }
        Category category=mapper.createCategoryDTOtoEntity(dto);
        try{
            repo.save(category);
            logger.info("Se ha registrado la categoría con nombre={} y id={} exitosamente", category.getName(), category.getId());
        }catch(PersistenceException ex){
            logger.error("No se ha podido registrar la categoría con nombre={}, Error: {}",category.getName(),ex.getMessage());
            throw new DatabaseErrorException("Error en la base de datos");
            
        }
        

        CategoryDetailsDTO finalDto=mapper.entityToCategoryDetailsDTO(category);
        return finalDto;

    }

    public List<CategoryListDTO>getAllCategories(){
        List<Category>categories=repo.findAll();
        List<CategoryListDTO> dtos=categories.stream().map(c->mapper.entityToCategoryListDTO(c)).toList();
        return dtos;
    }

    public CategoryDetailsDTO getCategoryDTOById(Long categoryId)throws NotFoundException{
        Category category=getCategoryEntityById(categoryId);
        CategoryDetailsDTO categoryDto=mapper.entityToCategoryDetailsDTO(category);
        return categoryDto;
    }

    @Transactional
    public CategoryDetailsDTO updateCategory(UpdateCategory dto, Long categoryId)throws NotFoundException,DatabaseErrorException{
        Category category=getCategoryEntityById(categoryId);
        mapper.updateEntity(dto, category);
        try{
            repo.save(category);
            logger.info("Se ha actualizado el producto con id={}", category.getId());
        }catch(PersistenceException ex){
            logger.error("No se ha podido actualizar la categoría con id={}, Error: {}", category.getId(), ex.getMessage());
            throw new DatabaseErrorException("Ha ocurrido un error en la base de datos");
        }
        
        return mapper.entityToCategoryDetailsDTO(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId)throws NotFoundException,InvalidRequestException,DatabaseErrorException{
        Category category=getCategoryEntityById(categoryId);
        if (!category.getProducts().isEmpty()) {
            throw new InvalidRequestException("No puedes eliminar esta categoría porque tiene productos asociaods");
        }

        try{
            repo.delete(category);
            logger.info("Eliminado exitosamente la categoría  con id={}",category.getId());

        }catch(PersistenceException ex){
            logger.error("Fallo al eliminar la categoría con id={}", category.getId());
            throw new DatabaseErrorException("Error en la base de datos");
            
        }
        
    }
}
