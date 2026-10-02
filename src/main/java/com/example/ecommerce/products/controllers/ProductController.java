package com.example.ecommerce.products.controllers;

import com.example.ecommerce.products.DTOs.CreateProductDTO;
import com.example.ecommerce.products.DTOs.ProductDetailsDTO;
import com.example.ecommerce.products.DTOs.ProductListDTO;
import com.example.ecommerce.products.DTOs.SearchProductDTO;
import com.example.ecommerce.products.DTOs.UpdateProductDTO;
import com.example.ecommerce.products.services.ProductService;
import com.example.ecommerce.products.utils.ProductSortCriteria;
import com.example.ecommerce.shared.utils.PaginationCriteria;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
  private final ProductService productService;

  @PostMapping()
  @PreAuthorize("hasAnyRole('ADMIN','MANAGER')and hasAuthority('EDIT_CATALOGUE')")
  public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductDTO dto) {
    ProductDetailsDTO product = productService.createProduct(dto);
    URI location = URI.create("/api/v1/products/" + product.id());

    return ResponseEntity.created(location).contentType(MediaType.APPLICATION_JSON).body(product);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDetailsDTO> getProduct(@PathVariable @Positive Long id) {
    return ResponseEntity.status(200)
        .contentType(MediaType.APPLICATION_JSON)
        .body(productService.findByIdAndReturnProductDetailsDto(id));
  }

  @GetMapping()
  public ResponseEntity<Page<ProductListDTO>> getAllProducts(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "true") Boolean ascending,
      @RequestParam(required = false, defaultValue = "id")
          ProductSortCriteria productSortCriteria) {
    PaginationCriteria<ProductSortCriteria> paginationCriteria =
        new PaginationCriteria<>(page, size, productSortCriteria, ascending);

    return ResponseEntity.status(200).body(productService.getAllProducts(paginationCriteria));
  }

  @PatchMapping("/{productId}")
  @PreAuthorize("hasAnyRole('ADMIN','MANAGER')and hasAuthority('EDIT_CATALOGUE')")
  public ResponseEntity<ProductDetailsDTO> updateProduct(
      @Valid @RequestBody UpdateProductDTO dto, @PathVariable @Positive Long productId) {

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
  public ResponseEntity<Page<ProductListDTO>> searchProducts(
      @RequestParam(required = false) Double minPrice,
      @RequestParam(required = false) Double maxPrice,
      @RequestParam(required = false, defaultValue = "0") Long categoryId,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "0") String name,
      @RequestParam(required = false, defaultValue = "10") Integer items,
      @RequestParam(required = false, defaultValue = "true") Boolean ascending,
      @RequestParam(required = false) ProductSortCriteria productSortCriteria) {
    ThreadContext.putAll(Map.of("use_case", "search_products", "entity", "product"));
    PaginationCriteria<ProductSortCriteria> paginationCriteria =
        new PaginationCriteria<>(page, items, productSortCriteria, ascending);
    SearchProductDTO searchProductDTO =
        new SearchProductDTO(name, minPrice, maxPrice, categoryId, paginationCriteria);
    return ResponseEntity.ok(productService.searchProducts(searchProductDTO));
  }
}
