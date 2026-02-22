package com.example.ecommerce.order.mappers.order;

import com.example.ecommerce.order.dtos.order.OrderDTO;
import com.example.ecommerce.order.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  @Mapping(source = "order.user.id", target = "userId")
  OrderDTO entityToOrderDTO(Order order);
}
