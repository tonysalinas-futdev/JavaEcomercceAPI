package com.example.ecommerce.cart.services;

import com.example.ecommerce.cart.dto.CartDetailsDTO;
import com.example.ecommerce.cart.dto.CreateCartItem;
import com.example.ecommerce.cart.mappers.CartMappers;
import com.example.ecommerce.cart.models.Cart;
import com.example.ecommerce.cart.models.CartItem;
import com.example.ecommerce.cart.repositories.CartRepository;
import com.example.ecommerce.products.model.Product;
import com.example.ecommerce.products.utils.ProductValidator;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserAdminService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartService {
  private final CartRepository repo;
  private final ProductValidator productValidator;
  private final CartItemService cartItemService;
  private final UserAdminService userSertvice;
  private final CartMappers mapper;

  public Cart getById(Long cartId) {
    return repo.findById(cartId).orElseThrow(() -> new NotFoundException("Cart not found"));
  }

  public CartDetailsDTO getCartDetailsDTO(Long cartId) {
    Cart cart = getById(cartId);

    return mapper.entityToCartDetailsDTO(cart);
  }

  private Cart getByCartItemId(Long cartItemId) {
    return repo.findByCartItems_Id(cartItemId)
        .orElseThrow(() -> new NotFoundException("Cart not found"));
  }

  @Transactional
  public Cart getByCartItemIdAndBlockRow(Long cartItemId) {
    return repo.findByIdForUpdate(cartItemId)
        .orElseThrow(() -> new NotFoundException("Cart not found"));
  }

  private Cart buildAndSaveCart(User user) {
    Cart saveCart = Cart.builder().user(user).build();
    repo.saveAndFlush(saveCart);
    return saveCart;
  }

  private Cart getOrCreateIfNotExists(Long cartId, User user) {
    if (cartId == null) {
      return buildAndSaveCart(user);
    }
    Optional<Cart> cart = repo.findById(cartId);
    if (cart.isPresent()) {
      return cart.get();
    }

    return buildAndSaveCart(user);
  }

  @Transactional
  public CartDetailsDTO addItem(Long cartId, String userEmail, CreateCartItem dto) {
    User user = userSertvice.getUserByEmail(userEmail);
    Cart cart = getOrCreateIfNotExists(cartId, user);
    Product productInCartItem =
        productValidator.validateAvaibilityAndStockAndReturn(dto.getProductId(), dto.getQuantity());

    CartItem existingItemInCart =
        cartItemService.getByProductAndCartId(dto.getProductId(), cart.getId());

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

  public Cart cleanCart(Long cartId) {
    Cart cart = getById(cartId);
    cart.getItems().clear();
    repo.saveAndFlush(cart);
    return cart;
  }

  public CartDetailsDTO deleteItemFromCart(Long cartItemId, Long cartId) {
    Cart cart = getByCartItemId(cartId);
    cart.getItems().removeIf(it -> it.getId().equals(cartItemId));
    repo.saveAndFlush(cart);
    CartDetailsDTO cartDetailsDTO = mapper.entityToCartDetailsDTO(cart);

    return cartDetailsDTO;
  }

  @Transactional
  public void deleteCart(Long cartId) {
    Cart cart = getById(cartId);
    repo.delete(cart);
  }
}
