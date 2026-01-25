package com.example.Ecomercce.cart.services;

import com.example.Ecomercce.cart.dto.CreateCartItem;
import com.example.Ecomercce.cart.models.Cart;
import com.example.Ecomercce.cart.models.CartItem;
import com.example.Ecomercce.cart.repositories.CartRepository;
import com.example.Ecomercce.products.model.Product;
import com.example.Ecomercce.products.utils.ValidateProduct;
import com.example.Ecomercce.shared.exceptions.InvalidRequestException;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.services.UserAdminService;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartService {
  private final CartRepository repo;
  private final UserAdminService userService;
  private final ValidateProduct productValidator;
  private final CartItemService cartItemService;

  private Cart getByCartItemId(Long cartItemId) {
    return repo.findByCartItemId(cartItemId)
        .orElseThrow(() -> new NotFoundException("No se ha encontrado el carrito"));
  }

  private void containsItem(Cart cart, CartItem cartItem) {
    if (!cart.getItems().contains(cartItem)) {
      throw new InvalidRequestException("Este objeto no pertenece a ese Carrito");
    }
  }

  private void cartExists(User user) {
    if (user.getCart() == null) {
      throw new InvalidRequestException("Primero debe añadir algún producto al carrito");
    }
  }

  private User getUserAndValidateCart(String userEmail) {
    User user = userService.getUserByEmail(userEmail);

    cartExists(user);
    return user;
  }

  public List<CartItem> getAllItemsOfCart(String userEmail) {
    User user = userService.getUserByEmail(userEmail);

    if (user.getCart() != null) {
      return user.getCart().getItems();
    }

    return Collections.emptyList();
  }

  private Cart buildAndSaveCart(User user) {
    Cart cart = Cart.builder().user(user).build();
    repo.saveAndFlush(cart);
    return cart;
  }

  public Cart addItem(String userEmail, CreateCartItem dto) {
    User user = userService.getUserByEmail(userEmail);
    Product product =
        productValidator.validateAvaibilityAndStockAndReturn(dto.getProductId(), dto.getQuantity());

    if (user.getCart() == null) {
      Cart cart = buildAndSaveCart(user);
      user.setCart(cart);
    }

    Cart cart = user.getCart();

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
    User refreshUser = userService.refreshUser(user);

    return refreshUser.getCart();
  }

  public Cart setItemQuantity(String userEmail, Long cartItemId, Integer quantity) {
    User user = getUserAndValidateCart(userEmail);
    Cart cart = user.getCart();

    CartItem item = cartItemService.getCartItemById(cartItemId);
    containsItem(cart, item);

    item.setQuantity(quantity);
    repo.save(cart);
    return cart;
  }

  public Cart cleanCart(String userEmail) {
    Cart cart = userService.getUserByEmail(userEmail).getCart();
    cart.getItems().clear();
    repo.saveAndFlush(cart);
    return cart;
  }

  public Cart deleteItemFromCart(Long cartItemId) {
    Cart cart = getByCartItemId(cartItemId);
    cart.getItems().removeIf(it -> it.getId().equals(cartItemId));
    repo.saveAndFlush(cart);
    return cart;
  }

  @Transactional
  public void deleteCart(String userEmail) {
    Cart cart = userService.getUserByEmail(userEmail).getCart();
    repo.delete(cart);
  }
}
