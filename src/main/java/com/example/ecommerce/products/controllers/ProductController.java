package com.example.ecommerce.products.controllers;

import com.example.ecommerce.products.DTOs.CreateProductDTO;
import com.example.ecommerce.products.DTOs.ProductDetailsDTO;
import com.example.ecommerce.products.DTOs.ProductListDTO;
import com.example.ecommerce.products.DTOs.SearchProductDTO;
import com.example.ecommerce.products.DTOs.UpdateProduct;
import com.example.ecommerce.products.services.ProductService;
import com.example.ecommerce.shared.dtos.paginatedresponse.PaginatedResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
  @PreAuthorize("hasAnyRole('ADMIN','MANAGER')and hasAuthority('EDIT_CATALOGUE')")
  public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductDTO dto) {
    ProductDetailsDTO product = productService.createProduct(dto);
    URI location = URI.create("/api/v1/products/" + product.getId());

    return ResponseEntity.created(location).contentType(MediaType.APPLICATION_JSON).body(product);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDetailsDTO> getProduct(@PathVariable @Positive Long id) {
    return ResponseEntity.status(200).body(productService.findByIdAndReturnProductDetailsDto(id));
  }

  @GetMapping()
  public ResponseEntity<PaginatedResponseDTO<ProductListDTO>> getAllProducts(
      @Valid SearchProductDTO dto) {

    return ResponseEntity.status(200).body(productService.searchProducts(dto));
  }

  @PatchMapping("/{productId}")
  @PreAuthorize("hasAnyRole('ADMIN','MANAGER')and hasAuthority('EDIT_CATALOGUE')")
  public ResponseEntity<ProductDetailsDTO> updateProduct(
      @Valid @RequestBody UpdateProduct dto, @PathVariable @Positive Long productId) {

    return ResponseEntity.status(200).body(productService.updateProduct(dto, productId));
  }

  @DeleteMapping("/{productId}")
  @PreAuthorize("hasAnyRole('ADMIN','MANAGER')and hasAuthority('EDIT_CATALOGUE')")
  public ResponseEntity<?> deleteProduct(@PathVariable @Positive Long productId) {
    productService.deleteProduct(productId);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{productId}/category/{categoryId}")
  @PreAuthorize("hasAnyRole('ADMIN','MANAGER')and hasAuthority('EDIT_CATALOGUE')")
  public ResponseEntity<ProductDetailsDTO> updateProductCategory(
      @Positive @PathVariable Long categoryId, @Positive @PathVariable Long productId) {

    return ResponseEntity.status(200).body(productService.updateCategory(categoryId, productId));
  }

  @GetMapping("/search")
  public ResponseEntity<PaginatedResponseDTO<ProductListDTO>> searchProducts(
      @Valid @ModelAttribute SearchProductDTO dto) {
    ThreadContext.putAll(Map.of("use_case", "search_products", "entity", "product"));

    return ResponseEntity.ok(productService.searchProducts(dto));
  }
}
