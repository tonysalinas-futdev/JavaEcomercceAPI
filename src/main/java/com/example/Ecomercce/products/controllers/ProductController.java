package com.example.Ecomercce.products.controllers;

import com.example.Ecomercce.products.DTOs.CreateProductDTO;
import com.example.Ecomercce.products.DTOs.ProductDetailsDTO;
import com.example.Ecomercce.products.DTOs.ProductListDTO;
import com.example.Ecomercce.products.DTOs.SearchProductDTO;
import com.example.Ecomercce.products.DTOs.UpdateProduct;
import com.example.Ecomercce.products.services.ProductService;
import com.example.Ecomercce.shared.DTOs.PaginatedDtos.PaginatedResponseDTO;
import com.example.Ecomercce.shared.exceptions.AlreadyExistsException;
import com.example.Ecomercce.shared.exceptions.DatabaseErrorException;
import com.example.Ecomercce.shared.exceptions.InvalidRequestException;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import java.net.URI;
import java.util.List;

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
  public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductDTO dto)
      throws DatabaseErrorException,
          InvalidRequestException,
          AlreadyExistsException,
          NotFoundException {
    ProductDetailsDTO product = productService.createProduct(dto);
    URI location=URI.create("/api/v1/products/"+product.getId());
    return ResponseEntity.created(location).body(product);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDetailsDTO> getProduct(@PathVariable @Positive Long id)
      throws NotFoundException {
    
    return ResponseEntity.status(200).body( productService.getProductById(id));
  }

  @GetMapping()
  public ResponseEntity<PaginatedResponseDTO<ProductListDTO>> getAllProduct(
      @Valid SearchProductDTO dto) throws NotFoundException {
    return ResponseEntity.status(200).body(productService.searchProducts(dto));
  }

  @PatchMapping("/{productId}")
  public ResponseEntity<ProductDetailsDTO> updateProduct(
      @Valid @RequestBody UpdateProduct dto, @PathVariable @Positive Long productId)
      throws NotFoundException, DatabaseErrorException {
    return ResponseEntity.status(200).body(productService.updateProduct(dto, productId));
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<?> deleteProduct(@PathVariable @Positive Long productId)
      throws NotFoundException, DatabaseErrorException {
    productService.deleteProduct(productId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{productId}/category/{categoryId}")
  public ResponseEntity<ProductDetailsDTO> updateProductCategory(
      @Positive @PathVariable Long categoryId, @Positive @PathVariable Long productId)
      throws NotFoundException, DatabaseErrorException {

    return ResponseEntity.status(200).body(productService.updateCategory(categoryId, productId));

  }
  
  @GetMapping()
  public ResponseEntity<PaginatedResponseDTO<ProductListDTO>>searchProducts(@Valid @ModelAttribute SearchProductDTO dto) throws NotFoundException{
    return ResponseEntity.ok(productService.searchProducts(dto));

  }
}
