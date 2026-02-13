package com.example.Ecomercce.cart.services;

import com.example.Ecomercce.cart.dto.CreateCartItem;
import com.example.Ecomercce.cart.models.Cart;
import com.example.Ecomercce.cart.models.CartItem;
import com.example.Ecomercce.cart.repositories.CartRepository;
import com.example.Ecomercce.products.model.Product;
import com.example.Ecomercce.products.utils.ValidateProduct;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.services.UserAdminService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartService {
  private final CartRepository repo;
  private final ValidateProduct productValidator;
  private final CartItemService cartItemService;
  private final UserAdminService userSertvice;

  public Cart getById(Long cartId) {
    return repo.findById(cartId).orElseThrow(() -> new NotFoundException("Cart not found"));
  }

  private Cart getByCartItemId(Long cartItemId) {
    return repo.findByCartItems_Id(cartItemId)
        .orElseThrow(() -> new NotFoundException("No se ha encontrado el carrito"));
  }

  @Transactional
  public Cart getByCartItemIdAndBlockRow(Long cartItemId) {
    return repo.findByIdForUpdate(cartItemId)
        .orElseThrow(() -> new NotFoundException("No se ha encontrado el carrito"));
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
  public Cart addItem(Long cartId, String userEmail, CreateCartItem dto) {
    User user = userSertvice.getUserByEmail(userEmail);
    Cart cart = getOrCreateIfNotExists(cartId, user);
    Product product =
        productValidator.validateAvaibilityAndStockAndReturn(dto.getProductId(), dto.getQuantity());

    CartItem existingCartItem =
        cartItemService.getByProductAndCartId(dto.getProductId(), cart.getId());

    if (existingCartItem != null) {
      existingCartItem.setQuantity(dto.getQuantity());
      repo.saveAndFlush(cart);
      return cart;
    }

    CartItem cartItem =
        CartItem.builder().product(product).cart(cart).quantity(dto.getQuantity()).build();

    cart.getItems().add(cartItem);
    repo.saveAndFlush(cart);
    return cart;
  }

  public Cart cleanCart(Long cartId) {
    Cart cart = getById(cartId);
    cart.getItems().clear();
    repo.saveAndFlush(cart);
    return cart;
  }

  public Cart deleteItemFromCart(Long cartItemId, Long cartId) {
    Cart cart = getByCartItemId(cartId);
    cart.getItems().removeIf(it -> it.getId().equals(cartItemId));
    repo.saveAndFlush(cart);
    return cart;
  }

  @Transactional
  public void deleteCart(Long cartId) {
    Cart cart = getById(cartId);
    repo.delete(cart);
  }
}
