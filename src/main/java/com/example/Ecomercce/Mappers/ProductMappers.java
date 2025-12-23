package com.example.Ecomercce.Mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.Ecomercce.DTOs.ProductDTOs.CreateProductDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.ProductAdminDetails;
import com.example.Ecomercce.DTOs.ProductDTOs.ProductDetailsDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.ProductListDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.UpdateProduct;
import com.example.Ecomercce.Models.Product;

@Mapper(componentModel = "spring")
public interface ProductMappers {
    
    Product createProductDTOToEntity(CreateProductDTO dto);

    ProductDetailsDTO productToDetailsDTO(Product product);

    ProductAdminDetails productToAdminDetailsDTO(Product product);

    ProductListDTO productToProductListDTO(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product updateEntity(UpdateProduct dto, @MappingTarget Product entity);
    

}
