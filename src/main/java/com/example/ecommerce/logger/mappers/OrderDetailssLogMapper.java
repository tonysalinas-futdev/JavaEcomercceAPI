package com.example.ecommerce.logger.mappers;

import com.example.ecommerce.logger.builders.audit.StructuredOrderDetailsEventLogWriter;
import com.example.ecommerce.order.dtos.orderdetails.OrderDetailsDTO;
import com.example.ecommerce.order.models.OrderDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailssLogMapper {
  @Mapping(source = "entity.order.id", target = "orderId")
  @Mapping(source = "entity.product.id", target = "productId")
  StructuredOrderDetailsEventLogWriter fromEntityToStructuredLog(OrderDetails entity);

  @Mapping(source = "dto.productId", target = "productId")
  StructuredOrderDetailsEventLogWriter fromDtoToStructuredLog(OrderDetailsDTO dto);
}
