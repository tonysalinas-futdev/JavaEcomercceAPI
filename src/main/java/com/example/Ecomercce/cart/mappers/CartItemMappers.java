package com.example.Ecomercce.cart.mappers;

import com.example.Ecomercce.cart.dto.CartItemDTO;
import com.example.Ecomercce.cart.dto.CreateCartItem;
import com.example.Ecomercce.cart.models.CartItem;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {CartMappers.class})
public interface CartItemMappers {
  CartItem createCartItemDTOToCartItemEntity(CreateCartItem dto);

  CartItemDTO entityToCartItemDto(CartItem entity);
}
