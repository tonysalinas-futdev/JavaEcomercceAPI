package com.example.ecommerce.testServices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.ecommerce.categories.model.Category;
import com.example.ecommerce.products.DTOs.CreateProductDTO;
import com.example.ecommerce.products.DTOs.ProductDetailsDTO;
import com.example.ecommerce.products.DTOs.ProductListDTO;
import com.example.ecommerce.products.DTOs.SearchProductDTO;
import com.example.ecommerce.products.DTOs.UpdateProduct;
import com.example.ecommerce.products.model.Product;
import com.example.ecommerce.products.services.ProductService;
import com.example.ecommerce.shared.dtos.paginatedresponse.PaginatedResponseDTO;
import com.example.ecommerce.shared.exceptions.AlreadyExistsException;
import com.example.ecommerce.shared.exceptions.InvalidRequestException;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Sql(
    scripts = {"/clean.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class ProductServiceTest {
  @Autowired private ProductService service;
  @Autowired private ProductTestServiceHelper conftest;

  public CreateProductDTO buildCreateProductDtoHelper(
      String name, String description, String pic, Double price, Integer stock, Long categoryId) {
    return CreateProductDTO.builder()
        .name(name)
        .description(description)
        .pic(pic)
        .price(price)
        .stock(stock)
        .categoryId(categoryId)
        .build();
  }

  @ParameterizedTest
  @CsvSource({
    "Cama, Una cama suave, dhfghfhg, 200.00, 50",
    "Armario,, ddsfsd, 50.0, 45",
    "Silla,,,400.0,"
  })
  void shouldCreateProductUsingCreateProductDto(
      String name, String description, String pic, Double price, Integer stock) {

    Category category = conftest.createCategory();
    CreateProductDTO dto =
        buildCreateProductDtoHelper(name, description, pic, price, stock, category.getId());
    ProductDetailsDTO product = service.createProduct(dto);

    assertTrue(product.getId() != null);
    assertTrue(product.getName().equals(name));
    assertTrue(product.getAvailable().equals(true));
  }

  @ParameterizedTest
  @CsvSource({
    "null, Un bonito armario, fsfsgs, 20.0, 40",
    "Helado, Un rico helado,, 60.0, 100",
    "Botas de agua, Muy buenas botas, eqweq, 70.0, 57"
  })
  void shouldFailWhenCreatingEachProduct(
      String name, String description, String pic, Double price, Integer stock) {

    conftest.createProductWithNameEqualHelado();
    Category category = conftest.createCategory();
    CreateProductDTO product =
        buildCreateProductDtoHelper(name, description, pic, price, stock, category.getId());

    if (name == null) {
      assertThrows(
          InvalidRequestException.class,
          () -> {
            service.createProduct(product);
          });

    } else if (name.equals("Helado")) {
      assertThrows(
          AlreadyExistsException.class,
          () -> {
            service.createProduct(product);
          });
    } else if (name.equals("Botas de agua")) {
      product.setCategoryId(40L);
      assertThrows(
          NotFoundException.class,
          () -> {
            service.createProduct(product);
          });
    }
  }

  @ParameterizedTest
  @CsvSource({
    "Chocolate, Un rico chocolate, dhfghfhg, 200.00, 50",
    "TV LG,, ddsfsd, 50.0, 45",
    "Coche Audi,,,400.0,"
  })
  @DirtiesContext
  void shouldUpdateProductWithData(
      String name, String description, String pic, Double price, Integer stock) {
    Product product = conftest.createProductWithNameEqualHelado();

    UpdateProduct updateData =
        UpdateProduct.builder()
            .name(name)
            .description(description)
            .pic(pic)
            .price(price)
            .stock(stock)
            .build();

    ProductDetailsDTO updatedProduct = service.updateProduct(updateData, product.getId());

    assertTrue(updatedProduct.getName().equals(name));
    assertEquals(product.getId(), updatedProduct.getId());

    if (description != null) {
      assertTrue(updatedProduct.getDescription().equals(description));

    } else {
      assertTrue(updatedProduct.getDescription().equals(product.getDescription()));
    }

    if (price != null) {
      assertEquals(updatedProduct.getPrice(), price);

    } else {
      assertEquals(updatedProduct.getPrice(), product.getPrice());
    }
  }

  @Test
  @Sql("/data.sql")
  void shouldSearchProducts() {
    SearchProductDTO data = SearchProductDTO.builder().maxPrice(130.0).minPrice(40.0).build();
    SearchProductDTO data2 = SearchProductDTO.builder().name("Laptop").build();

    PaginatedResponseDTO<ProductListDTO> products = service.searchProducts(data);
    PaginatedResponseDTO<ProductListDTO> laptops = service.searchProducts(data2);
    assertEquals(3, laptops.getTotalElements());
    assertTrue(products.getTotalElements() > 0);
  }
}
