package com.example.Ecomercce.order.mappers.order;

import com.example.Ecomercce.order.dtos.order.OrderDTO;
import com.example.Ecomercce.order.models.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  OrderDTO entityToOrderDTO(Order order);
}
