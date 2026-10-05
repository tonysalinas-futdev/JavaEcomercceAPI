package com.example.ecommerce.integrationTests.cart.cartservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.ecommerce.cart.models.Cart;
import com.example.ecommerce.cart.services.CartService;
import com.example.ecommerce.integrationTests.globalconftest.GlobalConftest;
import com.example.ecommerce.products.model.Product;
import com.example.ecommerce.products.services.ProductService;
import com.example.ecommerce.users.models.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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
  @Autowired private ProductService productService;
  @Autowired
    private RedisConnectionFactory factory;

    @BeforeEach
    void cleanRedis() {
        factory.getConnection()
                .serverCommands()
                .flushDb();
    }
  @Test
  public void mustCreateCartAndAddTwoItems() {
    User user = globalConftest.createUser();
    Product product = productService.findByIdOrThrow(16L);
    service.addItem(user.getId(),product.getId().intValue(),10);

    Cart cart = service.findByUserIdOrCreate(user.getId());
    assertEquals(1, cart.getItems().size());
  }

  @Test
  public void shouldSetNewProductQuantity(){
    User user = globalConftest.createUser();
    service.findByUserIdOrCreate(user.getId());
    service.addItem(user.getId(),17,40);

    service.setItemQuantity(user.getId(),17,5);
    Cart updatedCart = service.findByUserIdOrCreate(user.getId());
        Assertions.assertEquals(5,updatedCart.getItems().get(17));
  }



  @Test
  public void shouldCleanAllItemsFromCart() {
      User user = globalConftest.createUser();
      service.addItem(user.getId(),16,10);
    Cart cart = service.findByUserIdOrCreate(user.getId());
      service.addItem(user.getId(),16,10);

    service.cleanItems(user.getId());
      Cart updatedCart = service.findByUserIdOrCreate(user.getId());
    assertTrue(updatedCart.getItems().isEmpty());
  }

  @Test
  public void shouldDeleteItemFromCartWithTwoItems() {
      User user = globalConftest.createUser();
      Cart cart = service.findByUserIdOrCreate(user.getId());
      service.addItem(user.getId(),16,3);
      service.addItem(user.getId(),17,3);

    service.deleteItem(17, 1L);
     Cart updatedCart = service.findByUserIdOrCreate(user.getId());
    assertEquals(1, updatedCart.getItems().size());
  }
}
