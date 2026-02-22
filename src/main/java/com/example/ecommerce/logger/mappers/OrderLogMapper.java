package com.example.ecommerce.logger.mappers;

import com.example.ecommerce.logger.builders.audit.StructuredOrderEventLogWriter;
import com.example.ecommerce.order.dtos.order.OrderDTO;
import com.example.ecommerce.order.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderLogMapper {
  StructuredOrderEventLogWriter fromDtoToStructuredLog(OrderDTO dto);

  @Mapping(source = "entity.user.id", target = "userId")
  StructuredOrderEventLogWriter fromEntityToStructuredLog(Order entity);
}
