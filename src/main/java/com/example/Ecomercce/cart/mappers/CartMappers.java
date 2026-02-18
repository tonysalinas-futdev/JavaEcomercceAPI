package com.example.Ecomercce.cart.mappers;

import com.example.Ecomercce.cart.dto.CartDetailsDTO;
import com.example.Ecomercce.cart.models.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMappers {
  @Mapping(source = "user.id", target = "userId")
  CartDetailsDTO entityToCartDetailsDTO(Cart entity);
}
