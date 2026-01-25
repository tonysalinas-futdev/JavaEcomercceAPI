package com.example.Ecomercce.integrationTests.cart.cartservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.Ecomercce.cart.dto.CreateCartItem;
import com.example.Ecomercce.cart.models.Cart;
import com.example.Ecomercce.cart.services.CartService;
import com.example.Ecomercce.integrationTests.globalconftest.GlobalConftest;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestCartServices {
  @Autowired private GlobalConftest globalConftest;
  @Autowired private CartService service;
  @Autowired private UserService userService;

  @Test
  @Sql("/data.sql")
  public void testAddCartItem() {
    User user = globalConftest.createUser();
    CreateCartItem item =
        CreateCartItem.builder()
            .productId(Long.valueOf(1))
            .quantity(15)
            .userEmail(user.getEmail())
            .build();

    CreateCartItem item2 =
        CreateCartItem.builder()
            .productId(Long.valueOf(2))
            .quantity(12)
            .userEmail(user.getEmail())
            .build();

    service.addItem(user.getEmail(), item);
    service.addItem(user.getEmail(), item2);

    User refreshUser = userService.getUserByEmail(user.getEmail());

    assertTrue(refreshUser.getCart() != null);
    assertEquals(2, refreshUser.getCart().getItems().size());
  }

  @Test
  @Sql("/data.sql")
  public void addExistingProductToCart() {
    User user = globalConftest.createUser();

    CreateCartItem item =
        CreateCartItem.builder()
            .productId(Long.valueOf(1))
            .quantity(15)
            .userEmail(user.getEmail())
            .build();

    service.addItem(user.getEmail(), item);
    service.addItem(user.getEmail(), item);

    User refreshUser = userService.getUserByEmail(user.getEmail());

    assertTrue(refreshUser.getCart().getItems().size() == 1);
    assertTrue(refreshUser.getCart().getItems().get(0).getQuantity() == 15);
  }

  @Test
  @Sql("/data.sql")
  public void cleanCart() {

    User user = globalConftest.createUser();

    CreateCartItem item =
        CreateCartItem.builder()
            .productId(Long.valueOf(1))
            .quantity(15)
            .userEmail(user.getEmail())
            .build();

    service.addItem(user.getEmail(), item);
    service.addItem(user.getEmail(), item);

    User refreshUser = userService.getUserByEmail(user.getEmail());

    Cart cart = service.cleanCart(refreshUser.getEmail());

    assertTrue(cart.getItems().isEmpty());
  }
}
