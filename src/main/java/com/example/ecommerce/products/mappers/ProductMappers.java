package com.example.ecommerce.products.mappers;

import com.example.ecommerce.categories.mappers.CategoryMappers;
import com.example.ecommerce.products.DTOs.CreateProductDTO;
import com.example.ecommerce.products.DTOs.ProductAdminDetails;
import com.example.ecommerce.products.DTOs.ProductDetailsDTO;
import com.example.ecommerce.products.DTOs.ProductListDTO;
import com.example.ecommerce.products.DTOs.UpdateProduct;
import com.example.ecommerce.products.model.Product;
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
