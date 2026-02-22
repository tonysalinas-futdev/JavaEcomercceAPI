package com.example.ecommerce.cart.mappers;

import com.example.ecommerce.cart.dto.CartItemDTO;
import com.example.ecommerce.cart.dto.CreateCartItem;
import com.example.ecommerce.cart.models.CartItem;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {CartMappers.class})
public interface CartItemMappers {
  CartItem createCartItemDTOToCartItemEntity(CreateCartItem dto);

  CartItemDTO entityToCartItemDto(CartItem entity);
}
