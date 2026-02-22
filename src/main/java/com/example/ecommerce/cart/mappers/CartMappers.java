package com.example.ecommerce.cart.mappers;

import com.example.ecommerce.cart.dto.CartDetailsDTO;
import com.example.ecommerce.cart.models.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMappers {
  @Mapping(source = "user.id", target = "userId")
  CartDetailsDTO entityToCartDetailsDTO(Cart entity);
}
