package com.example.ecommerce.logger.mappers;

import com.example.ecommerce.logger.builders.audit.StructuredProductEventLog;
import com.example.ecommerce.products.DTOs.ProductDetailsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductLogMapper {
  StructuredProductEventLog productDetailsDtoToStructuredLog(ProductDetailsDTO dto);
}
