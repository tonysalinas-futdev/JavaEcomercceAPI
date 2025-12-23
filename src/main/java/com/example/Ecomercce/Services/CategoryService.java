package com.example.Ecomercce.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Ecomercce.DTOs.CategoryDTOs.CategoryDetailsDTO;
import com.example.Ecomercce.DTOs.CategoryDTOs.CategoryListDTO;
import com.example.Ecomercce.DTOs.CategoryDTOs.CreateCategoryDTO;
import com.example.Ecomercce.DTOs.CategoryDTOs.UpdateCategory;
import com.example.Ecomercce.Exceptions.AlreadyExistsException;
import com.example.Ecomercce.Exceptions.InvalidRequestException;
import com.example.Ecomercce.Exceptions.NotFoundException;
import com.example.Ecomercce.Mappers.CategoryMappers;
import com.example.Ecomercce.Repositories.CategoryRepository;

import jakarta.transaction.Transactional;

import com.example.Ecomercce.Models.Category;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryRepository repo;
    private CategoryMappers mapper;

    private Category getCategoryEntityById(Long categoryId) throws NotFoundException{
        Category category=repo.findById(categoryId).orElseThrow(()-> new NotFoundException("No se ha encontrado esa categoría"));
        return category;
    }


    @Transactional
    public CategoryDetailsDTO createCategory(CreateCategoryDTO dto)throws AlreadyExistsException{
        if (repo.getByName(dto.getName()).isPresent()) {
            throw new AlreadyExistsException("Ya existe una categoría con ese nombre");
        }
        Category category=mapper.createCategoryDTOtoEntity(dto);

        repo.save(category);

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
    public CategoryDetailsDTO updateCategory(UpdateCategory dto, Long categoryId)throws NotFoundException{
        Category category=getCategoryEntityById(categoryId);
        mapper.updateEntity(dto, category);
        repo.save(category);
        return mapper.entityToCategoryDetailsDTO(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId)throws NotFoundException,InvalidRequestException{
        Category category=getCategoryEntityById(categoryId);
        if (!category.getProducts().isEmpty()) {
            throw new InvalidRequestException("No puedes eliminar esta categoría porque tiene productos asociaods");
        }

        repo.delete(category);
    }
}
