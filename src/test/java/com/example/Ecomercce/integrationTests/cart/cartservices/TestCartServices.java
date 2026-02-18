package com.example.Ecomercce.integrationTests.cart.cartservices;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.Ecomercce.cart.dto.CartDetailsDTO;
import com.example.Ecomercce.cart.dto.CreateCartItem;
import com.example.Ecomercce.cart.models.Cart;
import com.example.Ecomercce.cart.services.CartService;
import com.example.Ecomercce.integrationTests.globalconftest.GlobalConftest;
import com.example.Ecomercce.users.models.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class TestCartServices {
  @Autowired private GlobalConftest globalConftest;
  @Autowired private CartService service;

  private CreateCartItem buildCartItemHelper(Long productId, Integer quantity) {
    return CreateCartItem.builder().productId(productId).quantity(quantity).build();
  }

  @Test
  public void mustCreateCartAndAddTwoItems() {
    User user = globalConftest.createUser();
    CreateCartItem item = buildCartItemHelper(1L, 15);
    CreateCartItem item2 = buildCartItemHelper(2L, 12);

    CartDetailsDTO cartWithOneItem = service.addItem(null, user.getEmail(), item);
    CartDetailsDTO cartWithTwoItems =
        service.addItem(cartWithOneItem.getId(), user.getEmail(), item2);

    assertEquals(2, cartWithTwoItems.getItems().size());
  }

  @Test
  public void shouldSetQuantity20ToExistingCartItemWithQuantity15() {
    User user = globalConftest.createUser();
    CreateCartItem item = buildCartItemHelper(1L, 10);
    CreateCartItem existingItemWithQuantity20 = buildCartItemHelper(1L, 15);

    CartDetailsDTO cartWithItemQuantity15 = service.addItem(null, user.getEmail(), item);
    CartDetailsDTO cartWithExistingItemQuantity20 =
        service.addItem(
            cartWithItemQuantity15.getId(), user.getEmail(), existingItemWithQuantity20);

    assertEquals(1, cartWithExistingItemQuantity20.getItems().size());
    assertEquals(15, cartWithExistingItemQuantity20.getItems().get(0).getQuantity());
  }

  @Test
  public void shouldCleanAllItemsFromCart() {
    Cart cart = service.getById(1L);

    assertEquals(3, cart.getItems().size());
  }

  @Test
  public void shouldDeleteItemFromCartWithThreeItems() {

    service.deleteItemFromCart(1L, 1L);
    Cart cart = service.getById(1L);

    assertEquals(2, cart.getItems().size());
  }
}
