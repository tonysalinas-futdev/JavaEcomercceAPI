package com.example.ecommerce.integrationTests.products;

import static org.hamcrest.Matchers.equalTo;

import com.example.ecommerce.auth.dtos.AuthResponse;
import com.example.ecommerce.integrationTests.globalconftest.GlobalConftest;
import com.example.ecommerce.products.DTOs.CreateProductDTO;
import com.example.ecommerce.products.DTOs.UpdateProduct;
import com.example.ecommerce.products.services.ProductService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestProductControllers {
  @Autowired private ProductService productService;
  @Autowired private GlobalConftest globalConftest;

  private AuthResponse adminCredentials;
  private AuthResponse userCredentials;
  private AuthResponse managerCredentials;

  @BeforeAll
  public void createUsersAndSetCredentials() {
    globalConftest.createAdmin();
    globalConftest.createManager();
    globalConftest.createUser();
    adminCredentials = globalConftest.obtainAdminCredentials();
    userCredentials = globalConftest.obtainUserCredentials();
    managerCredentials = globalConftest.obtainManagerCredentials();
  }

  private CreateProductDTO buildProductDTOHelper() {
    return CreateProductDTO.builder()
        .name("Zapatos")
        .description("Unos Zapatos")
        .stock(30)
        .price(40.00)
        .categoryId(3L)
        .build();
  }

  @Test
  public void testCreateProductController() {
    CreateProductDTO dto = buildProductDTOHelper();

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminCredentials.getAccessToken())
        .body(dto)
        .log()
        .all()
        .when()
        .post("http://localhost:8000/api/v1/products")
        .then()
        .body("price", equalTo(40.0f))
        .body("name", equalTo("Zapatos"))
        .log()
        .all()
        .statusCode(201);
  }

  @Test
  public void shouldReturn400WhenCreatingProductWithoutName() {

    CreateProductDTO dto =
        CreateProductDTO.builder()
            .name("")
            .description("Unos Zapatos")
            .stock(30)
            .price(40.00)
            .categoryId(3L)
            .build();

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminCredentials.getAccessToken())
        .body(dto)
        .log()
        .all()
        .when()
        .post("http://localhost:8000/api/v1/products")
        .then()
        .log()
        .all()
        .statusCode(400); // Expected 400
  }

  @Test
  public void shouldReturn409WhenCreatingProductWithExistingName() {

    CreateProductDTO dto =
        CreateProductDTO.builder()
            .name("Tenis adidas")
            .description("Unos Zapatos")
            .stock(30)
            .price(40.00)
            .categoryId(3L)
            .build();
    productService.createProduct(dto);

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + managerCredentials.getAccessToken())
        .body(dto)
        .log()
        .all()
        .when()
        .post("http://localhost:8000/api/v1/products")
        .then()
        .log()
        .all()
        .statusCode(409); // Expected 409
  }

  @Test
  public void shouldReturn200WhenGettingProductByIdEqual1() {
    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + userCredentials.getAccessToken())
        .when()
        .get("http://localhost:8000/api/v1/products/1")
        .then()
        .body("name", equalTo("Laptop Lenovo ThinkPad")) // Expected Laptop Lenovo ThinkPad
        .log()
        .all()
        .statusCode(200); // Expected 200
  }

  @Test
  public void shouldReturn404WhenGettingNoExistingProductId() {

    RestAssured.given()
        .contentType("application/json")
        .when()
        .get("http://localhost:8000/api/v1/products/500")
        .then()
        .body("message", equalTo("Product not found"))
        .log()
        .all()
        .statusCode(404); // Expected 404
  }

  @Test
  public void shouldReturn200WhenUpdatingProductWithIdEqual2() {
    UpdateProduct dto = UpdateProduct.builder().name("Laptop Toshiba").price(399.99).build();

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + managerCredentials.getAccessToken())
        .body(dto)
        .log()
        .all()
        .when()
        .patch("http://localhost:8000/api/v1/products/2")
        .then()
        .body("name", equalTo("Laptop Toshiba")) // Expected "Laptop Toshiba"
        .log()
        .all()
        .statusCode(200); // Expected 200
  }

  @Test
  public void shouldReturn409WhenUpdatingProductNameToExistingOne() {

    UpdateProduct dto =
        UpdateProduct.builder()
            .name("Auriculares Sony WH-1000XM5")
            .description("Unos auriculares")
            .price(40.0)
            .build();

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + managerCredentials.getAccessToken())
        .body(dto)
        .log()
        .all()
        .when()
        .patch("http://localhost:8000/api/v1/products/2")
        .then()
        .body("message", equalTo("Product already exists"))
        .log()
        .all()
        .statusCode(409); // Expected 409
  }
}
