package com.example.ecommerce.order.mappers.orderdetails;

import com.example.ecommerce.order.dtos.orderdetails.OrderDetailsDTO;
import com.example.ecommerce.order.mappers.order.OrderMapper;
import com.example.ecommerce.order.models.OrderDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {OrderMapper.class})
public interface OrderDetailsMapper {
  @Mapping(source = "entity.order.id", target = "orderId")
  @Mapping(source = "entity.product.id", target = "productId")
  OrderDetailsDTO entityToOrderDetailsDTO(OrderDetails entity);
}
