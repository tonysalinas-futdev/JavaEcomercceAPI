package com.example.ecommerce.cart.services;

import com.example.ecommerce.cart.dto.CartDetailsDTO;
import com.example.ecommerce.cart.dto.CreateCartItem;
import com.example.ecommerce.cart.mappers.CartMappers;
import com.example.ecommerce.cart.models.Cart;
import com.example.ecommerce.cart.models.CartItem;
import com.example.ecommerce.cart.repositories.CartRepository;
import com.example.ecommerce.products.services.ProductService;
import com.example.ecommerce.products.utils.ProductValidator;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserQueryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartService {
  private final CartRepository repo;
  private final CartItemService cartItemService;
  private final ProductService productService;
  private final UserQueryService userQueryService;
  private final CartMappers mapper;

  public Cart findByIdOrThrow(Long cartId) {
    return repo.findById(cartId).orElseThrow(() -> new NotFoundException("Cart not found"));
  }

  public CartDetailsDTO findByIdAndReturnCartDetailsDtoOrThrow(Long cartId) {
    Cart cart = findByIdOrThrow(cartId);
    return mapper.entityToCartDetailsDTO(cart);
  }

  private Cart findByCartItemIdOrThrow(Long cartItemId) {
    return repo.findByCartItems_Id(cartItemId)
        .orElseThrow(() -> new NotFoundException("Cart not found"));
  }

  @Transactional
  public Cart findByCartItemIdAndBlockRow(Long cartItemId) {
    return repo.findByIdForUpdate(cartItemId)
        .orElseThrow(() -> new NotFoundException("Cart not found"));
  }

  private Cart findByIdOrCreateIfNotExists(Long cartId, User user) {
    if (cartId == null || repo.findById(cartId) == null) {
      Cart saveCart = Cart.builder().user(user).build();
      repo.saveAndFlush(saveCart);
      return saveCart;
    }

    return findByIdOrThrow(cartId);
  }

  @Transactional
  public CartDetailsDTO addItem(Long cartId, String userEmail, CreateCartItem dto) {
    User user = userQueryService.findByEmailOrThrow(userEmail);
    Cart cart = findByIdOrCreateIfNotExists(cartId, user);
    var productInCartItem = productService.findByIdOrThrow(dto.getProductId());
    ProductValidator.validateAvaibilityAndStockAndReturn(productInCartItem, dto.getQuantity());

    CartItem existingItemInCart =
        cartItemService.findByProductAndCartId(dto.getProductId(), cart.getId());

    if (existingItemInCart != null) {
      existingItemInCart.setQuantity(dto.getQuantity());
      repo.saveAndFlush(cart);
      CartDetailsDTO cartDto = mapper.entityToCartDetailsDTO(cart);
      return cartDto;
    }

    CartItem cartItem =
        CartItem.builder()
            .product(productInCartItem)
            .cart(cart)
            .quantity(dto.getQuantity())
            .build();

    cart.getItems().add(cartItem);
    repo.saveAndFlush(cart);
    return mapper.entityToCartDetailsDTO(cart);
  }

  @Transactional
  public Cart cleanCart(Long cartId) {
    Cart cart = findByIdOrThrow(cartId);
    cart.getItems().clear();
    repo.saveAndFlush(cart);
    return cart;
  }

  public CartDetailsDTO deleteItemFromCart(Long cartItemId, Long cartId) {
    Cart cart = findByCartItemIdOrThrow(cartId);
    cart.getItems().removeIf(it -> it.getId().equals(cartItemId));
    repo.saveAndFlush(cart);
    CartDetailsDTO cartDetailsDTO = mapper.entityToCartDetailsDTO(cart);
    return cartDetailsDTO;
  }

  @Transactional
  public void deleteCart(Long cartId) {
    Cart cart = findByIdOrThrow(cartId);
    repo.delete(cart);
  }
}
