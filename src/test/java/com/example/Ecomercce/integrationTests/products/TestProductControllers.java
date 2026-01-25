package com.example.Ecomercce.integrationTests.products;

import static org.hamcrest.Matchers.equalTo;

import com.example.Ecomercce.auth.dtos.AuthResponse;
import com.example.Ecomercce.categories.repository.CategoryRepository;
import com.example.Ecomercce.integrationTests.globalconftest.GlobalConftest;
import com.example.Ecomercce.products.DTOs.CreateProductDTO;
import com.example.Ecomercce.products.DTOs.UpdateProduct;
import com.example.Ecomercce.products.services.ProductService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestProductControllers {
  @Autowired private ProductService productService;
  @Autowired private ProductControllersConftest conftest;
  @Autowired private CategoryRepository categoryRepo;
  @Autowired private GlobalConftest globalConftest;

  @Test
  @Sql("/data.sql")
  public void testCreateProductController() {
    AuthResponse response = globalConftest.obtainAdminCredentials();
    if (categoryRepo.getByName("CategoriaPrueba").isEmpty()) {
      conftest.returnCategory();
    }

    CreateProductDTO dto =
        CreateProductDTO.builder()
            .name("Zapatos")
            .description("Unos Zapatos")
            .stock(30)
            .price(40.00)
            .categoryId(Long.valueOf(1))
            .build();

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + response.getAccessToken())
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
  @Sql("/data.sql")
  public void createProductWithoutName() {
    AuthResponse response = globalConftest.obtainAdminCredentials();

    if (categoryRepo.getByName("CategoriaPrueba").isEmpty()) {
      conftest.returnCategory();
    }

    CreateProductDTO dto =
        CreateProductDTO.builder()
            .name("")
            .description("Unos Zapatos")
            .stock(30)
            .price(40.00)
            .categoryId(Long.valueOf(1))
            .build();

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + response.getAccessToken())
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
  @Sql("/data.sql")
  public void createExistingProduct() {
    AuthResponse response = globalConftest.obtainManagerCredentials();

    if (categoryRepo.getByName("CategoriaPrueba").isEmpty()) {
      conftest.returnCategory();
    }

    CreateProductDTO dto =
        CreateProductDTO.builder()
            .name("Tenis adidas")
            .description("Unos Zapatos")
            .stock(30)
            .price(40.00)
            .categoryId(Long.valueOf(1))
            .build();
    productService.createProduct(dto);

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + response.getAccessToken())
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
  @Sql("/data.sql")
  public void testGetProductByIdHappyPath() {
    AuthResponse response = globalConftest.obtainUserCredentials();
    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + response.getAccessToken())
        .when()
        .get("http://localhost:8000/api/v1/products/1")
        .then()
        .body("name", equalTo("Laptop Lenovo ThinkPad")) // Expected Laptop Lenovo ThinkPad
        .log()
        .all()
        .statusCode(200); // Expected 200
  }

  @Test
  @Sql("/data.sql")
  public void testGetProductByIdFailed() {

    RestAssured.given()
        .contentType("application/json")
        .when()
        .get("http://localhost:8000/api/v1/products/500")
        .then()
        .body("message", equalTo("No se ha podido encontrar el producto"))
        .log()
        .all()
        .statusCode(404); // Expected 404
  }

  @Test
  @Sql("/data.sql")
  public void testUpdateProductEndpoint() {
    AuthResponse response = globalConftest.obtainManagerCredentials();

    UpdateProduct dto = UpdateProduct.builder().name("Laptop Toshiba").price(399.99).build();

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + response.getAccessToken())
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
  @Sql("/data.sql")
  public void testUpdateProductFail() {
    AuthResponse response = globalConftest.obtainManagerCredentials();

    UpdateProduct dto =
        UpdateProduct.builder()
            .name("Auriculares Sony WH-1000XM5")
            .description("Unos auriculares")
            .price(40.0)
            .build();

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + response.getAccessToken())
        .body(dto)
        .log()
        .all()
        .when()
        .patch("http://localhost:8000/api/v1/products/2")
        .then()
        .body("message", equalTo("Ya existe un producto con ese nombre"))
        .log()
        .all()
        .statusCode(409); // Expected 409
  }
}
