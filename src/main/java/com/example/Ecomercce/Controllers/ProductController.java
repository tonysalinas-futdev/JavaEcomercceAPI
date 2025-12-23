package com.example.Ecomercce.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecomercce.DTOs.PaginatedDtos.PaginatedResponseDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.CreateProductDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.ProductDetailsDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.ProductListDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.SearchProductDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.UpdateProduct;
import com.example.Ecomercce.Exceptions.AlreadyExistsException;
import com.example.Ecomercce.Exceptions.InvalidRequestException;
import com.example.Ecomercce.Exceptions.NotFoundException;
import com.example.Ecomercce.Services.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@Validated
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?>saveProduct(@RequestBody @Valid CreateProductDTO dto) throws InvalidRequestException, AlreadyExistsException{
        ProductDetailsDTO product=productService.createProduct(dto);

        return ResponseEntity.status(201).body(product);
    }

    @GetMapping("/get_product/{id}")
    public ResponseEntity<ProductDetailsDTO>getProduct(@PathVariable @Positive Long id)throws NotFoundException{
        ProductDetailsDTO product=productService.getProductById(id);
        return ResponseEntity.status(200).body(product);

    }

    @GetMapping("/search_products")
    public ResponseEntity<PaginatedResponseDTO<ProductListDTO>>getAllProduct (@Valid SearchProductDTO dto) throws NotFoundException{
        return ResponseEntity.status(200).body(productService.searchProducts(dto));
    }

    @PatchMapping("/update_product/{productId}")
    public ResponseEntity<ProductDetailsDTO>updateProduct(@Valid UpdateProduct dto, @PathVariable @Positive Long productId)throws NotFoundException{
        return ResponseEntity.status(200).body(productService.updateProduct(dto, productId));
    }

    @DeleteMapping("/delete_product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable @Positive Long productId)throws NotFoundException{
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }

    @PatchMapping("/update_product_category")
    public ResponseEntity<ProductDetailsDTO>updateProductCategory(@Positive Long categoryId, @Positive Long productId) throws NotFoundException{
        
        return ResponseEntity.status(200).body(productService.updateCategory(categoryId, productId));}
    

}