package com.example.Ecomercce.order.mappers.orderdetails;

import com.example.Ecomercce.order.dtos.orderdetails.OrderDetailsDTO;
import com.example.Ecomercce.order.mappers.order.OrderMapper;
import com.example.Ecomercce.order.models.OrderDetails;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {OrderMapper.class})
public interface OrderDetailsMapper {
  OrderDetailsDTO entityToOrderDetailsDTO(OrderDetails entity);
}
