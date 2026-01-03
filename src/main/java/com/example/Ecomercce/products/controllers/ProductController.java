package com.example.Ecomercce.products.controllers;

import com.example.Ecomercce.products.DTOs.CreateProductDTO;
import com.example.Ecomercce.products.DTOs.ProductDetailsDTO;
import com.example.Ecomercce.products.DTOs.ProductListDTO;
import com.example.Ecomercce.products.DTOs.SearchProductDTO;
import com.example.Ecomercce.products.DTOs.UpdateProduct;
import com.example.Ecomercce.products.services.ProductService;
import com.example.Ecomercce.shared.DTOs.paginatedDtos.PaginatedResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
  private final ProductService productService;

  @PostMapping()
  public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductDTO dto) {
    ProductDetailsDTO product = productService.createProduct(dto);
    URI location = URI.create("/api/v1/products/" + product.getId());
    ThreadContext.put("use_case", "create_product");
    ThreadContext.put("entity", "product");

    return ResponseEntity.created(location).body(product);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDetailsDTO> getProduct(@PathVariable @Positive Long id) {
    ThreadContext.putAll(Map.of("use_case", "get_product_by_id", "entity", "product"));
    return ResponseEntity.status(200).body(productService.getProductById(id));
  }

  @GetMapping()
  public ResponseEntity<PaginatedResponseDTO<ProductListDTO>> getAllProducts(
      @Valid SearchProductDTO dto) {
    ThreadContext.putAll(Map.of("use_case", "get_all_products", "entity", "product"));
    return ResponseEntity.status(200).body(productService.searchProducts(dto));
  }

  @PatchMapping("/{productId}")
  public ResponseEntity<ProductDetailsDTO> updateProduct(
      @Valid @RequestBody UpdateProduct dto, @PathVariable @Positive Long productId) {
    ThreadContext.putAll(Map.of("use_case", "update_product", "entity", "product"));

    return ResponseEntity.status(200).body(productService.updateProduct(dto, productId));
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<?> deleteProduct(@PathVariable @Positive Long productId) {
    productService.deleteProduct(productId);
    ThreadContext.putAll(Map.of("use_case", "delete_product", "entity", "product"));

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{productId}/category/{categoryId}")
  public ResponseEntity<ProductDetailsDTO> updateProductCategory(
      @Positive @PathVariable Long categoryId, @Positive @PathVariable Long productId) {
    ThreadContext.putAll(Map.of("use_case", "update_product_category", "entity", "product"));

    return ResponseEntity.status(200).body(productService.updateCategory(categoryId, productId));
  }

  @GetMapping("/search")
  public ResponseEntity<PaginatedResponseDTO<ProductListDTO>> searchProducts(
      @Valid @ModelAttribute SearchProductDTO dto) {
    ThreadContext.putAll(Map.of("use_case", "search_products", "entity", "product"));

    return ResponseEntity.ok(productService.searchProducts(dto));
  }
}
