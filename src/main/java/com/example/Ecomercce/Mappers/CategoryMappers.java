package com.example.Ecomercce.Mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import com.example.Ecomercce.DTOs.CategoryDTOs.CategoryDetailsDTO;
import com.example.Ecomercce.DTOs.CategoryDTOs.CategoryListDTO;
import com.example.Ecomercce.DTOs.CategoryDTOs.CreateCategoryDTO;
import com.example.Ecomercce.DTOs.CategoryDTOs.UpdateCategory;
import com.example.Ecomercce.Models.Category;
@Mapper(componentModel = "spring")
public interface CategoryMappers {

    Category createCategoryDTOtoEntity(CreateCategoryDTO dto);

    CategoryDetailsDTO entityToCategoryDetailsDTO(Category entity);

    CategoryListDTO entityToCategoryListDTO(Category entity);

    @BeanMapping(nullValuePropertyMappingStrategy =NullValuePropertyMappingStrategy.IGNORE)
    Category updateEntity (UpdateCategory dto, @MappingTarget Category entity );

}
