package com.example.Ecomercce.products.mappers;

import com.example.Ecomercce.categories.mappers.CategoryMappers;
import com.example.Ecomercce.products.DTOs.CreateProductDTO;
import com.example.Ecomercce.products.DTOs.ProductAdminDetails;
import com.example.Ecomercce.products.DTOs.ProductDetailsDTO;
import com.example.Ecomercce.products.DTOs.ProductListDTO;
import com.example.Ecomercce.products.DTOs.UpdateProduct;
import com.example.Ecomercce.products.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    uses = {CategoryMappers.class})
public interface ProductMappers {

  Product createProductDTOToEntity(CreateProductDTO dto);

  ProductDetailsDTO productToDetailsDTO(Product product);

  ProductAdminDetails productToAdminDetailsDTO(Product product);

  ProductListDTO productToProductListDTO(Product product);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Product updateEntity(UpdateProduct dto, @MappingTarget Product entity);
}
