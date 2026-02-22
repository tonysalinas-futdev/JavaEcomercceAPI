package com.example.ecommerce.categories.mappers;

import com.example.ecommerce.categories.categoryDTOs.CategoryDetailsDTO;
import com.example.ecommerce.categories.categoryDTOs.CategoryListDTO;
import com.example.ecommerce.categories.categoryDTOs.CreateCategoryDTO;
import com.example.ecommerce.categories.categoryDTOs.UpdateCategory;
import com.example.ecommerce.categories.model.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CategoryMappers {

  Category createCategoryDTOtoEntity(CreateCategoryDTO dto);

  CategoryDetailsDTO entityToCategoryDetailsDTO(Category entity);

  CategoryListDTO entityToCategoryListDTO(Category entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Category updateEntity(UpdateCategory dto, @MappingTarget Category entity);
}
