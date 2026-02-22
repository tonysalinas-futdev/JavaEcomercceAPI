package com.example.ecommerce.products.services;

import com.example.ecommerce.categories.model.Category;
import com.example.ecommerce.categories.service.CategoryService;
import com.example.ecommerce.logger.annotations.LogDeleteEntityEvent;
import com.example.ecommerce.logger.annotations.LogProductEvent;
import com.example.ecommerce.products.DTOs.CreateProductDTO;
import com.example.ecommerce.products.DTOs.ProductDetailsDTO;
import com.example.ecommerce.products.DTOs.ProductListDTO;
import com.example.ecommerce.products.DTOs.SearchProductDTO;
import com.example.ecommerce.products.DTOs.UpdateProduct;
import com.example.ecommerce.products.logs.ProductLogEvent;
import com.example.ecommerce.products.mappers.ProductMappers;
import com.example.ecommerce.products.model.Product;
import com.example.ecommerce.products.repositories.ProductRepository;
import com.example.ecommerce.products.specifications.ProductSpecifications;
import com.example.ecommerce.shared.dtos.paginatedresponse.PaginatedResponseDTO;
import com.example.ecommerce.shared.exceptions.AlreadyExistsException;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.shared.exceptions.PersistenceErrorException;
import com.example.ecommerce.shared.utils.PageableUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {
  private final ProductRepository productRepo;
  private final ProductMappers productMapper;
  private final CategoryService categoryService;

  public Product getEntityByName(String productName) {
    return productRepo
        .getByName(productName)
        .orElseThrow(() -> new NotFoundException("Product not found"));
  }

  public Product getProductEntityById(Long productId) {
    return productRepo
        .findById(productId)
        .orElseThrow(() -> new NotFoundException("Product not found"));
  }

  @Transactional
  public Product getProductEntityByIdAndBlockRow(Long productId) {
    return productRepo
        .findByIdForUpdate(productId)
        .orElseThrow(() -> new NotFoundException("Product not found"));
  }

  @Transactional
  @LogProductEvent(loggerName = ProductService.class, event = ProductLogEvent.PRODUCT_STOCK_UPDATE)
  public ProductDetailsDTO setStock(Long productId, Integer stock) {
    var product = getProductEntityById(productId);
    product.setStock(stock);
    productRepo.save(product);
    return productMapper.productToDetailsDTO(product);
  }

  @Transactional
  @LogProductEvent(loggerName = ProductService.class, event = ProductLogEvent.PRODUCT_CREATED)
  public ProductDetailsDTO createProduct(CreateProductDTO dto) {
    if (productRepo.getByName(dto.getName()).isPresent()) {
      throw new AlreadyExistsException("Already exists product with name: " + dto.getName());
    }

    Product newProduct = productMapper.createProductDTOToEntity(dto);

    Category category = categoryService.getCategoryEntityById(dto.getCategoryId());

    newProduct.setCategory(category);
    newProduct.setAvailable(true);

    try {
      productRepo.saveAndFlush(newProduct);
      return productMapper.productToDetailsDTO(newProduct);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Database Error", ex);
    }
  }

  public ProductDetailsDTO getProductById(Long id) {
    Product product = getProductEntityById(id);
    return productMapper.productToDetailsDTO(product);
  }

  public PaginatedResponseDTO<ProductListDTO> getAllProducts(Integer page, Integer size) {
    page = PageableUtils.verifyPage(page);
    size = PageableUtils.verifySize(size);

    Pageable pageable = PageRequest.of(page, size);
    Page<Product> products = productRepo.findAll(pageable);
    List<ProductListDTO> productList =
        products.stream().map(p -> productMapper.productToProductListDTO(p)).toList();
    return new PaginatedResponseDTO<ProductListDTO>(
        productList,
        products.hasNext(),
        products.hasPrevious(),
        products.getNumber(),
        products.getTotalPages(),
        products.getTotalElements(),
        products.getNumberOfElements(),
        products.getSize());
  }

  @Transactional
  @LogDeleteEntityEvent(event = "PRODUCT_DELETED", loggerName = ProductService.class)
  public void deleteProduct(Long id) {
    Product product = getProductEntityById(id);
    try {
      productRepo.delete(product);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Database Error", ex);
    }
  }

  @Transactional
  @LogProductEvent(loggerName = ProductService.class, event = ProductLogEvent.PRODUCT_UPDATED)
  public ProductDetailsDTO updateProduct(UpdateProduct dto, Long productId) {
    if (productRepo.getByName(dto.getName()).isPresent()) {
      throw new AlreadyExistsException("Product already exists");
    }
    Product updatedProduct = productMapper.updateEntity(dto, getProductEntityById(productId));
    try {
      productRepo.save(updatedProduct);

    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Database error", ex);
    }

    return productMapper.productToDetailsDTO(updatedProduct);
  }

  @Transactional
  public ProductDetailsDTO updateCategory(Long categoryId, Long productId) {
    Category category = categoryService.getCategoryEntityById(categoryId);

    Product product = getProductEntityById(productId);

    product.setCategory(category);
    try {
      productRepo.save(product);

    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Database error", ex);
    }

    return productMapper.productToDetailsDTO(product);
  }

  public PaginatedResponseDTO<ProductListDTO> searchProducts(SearchProductDTO dto) {

    Specification<Product> specification =
        ProductSpecifications.hasName(dto.getName())
            .and(ProductSpecifications.filterByMaxPrice(dto.getMaxPrice()))
            .and(ProductSpecifications.filterbyMinPrice(dto.getMinPrice()));

    if (dto.getCategoryId() != null) {
      Category category = categoryService.getCategoryEntityById(dto.getCategoryId());
      specification = specification.and(ProductSpecifications.filterByCategory(category));
    }

    Page<Product> products =
        productRepo.findAll(specification, PageRequest.of(dto.getPage(), dto.getSize()));

    List<ProductListDTO> listOfProductsDTO =
        products.stream().map(p -> productMapper.productToProductListDTO(p)).toList();

    return PaginatedResponseDTO.<ProductListDTO>builder()
        .items(listOfProductsDTO)
        .hasNext(products.hasNext())
        .hasPrevious(products.hasPrevious())
        .page(products.getNumber())
        .size(products.getSize())
        .numberOfElementsInPage(products.getNumberOfElements())
        .totalPages(products.getTotalPages())
        .totalElements(products.getTotalElements())
        .build();
  }
}
