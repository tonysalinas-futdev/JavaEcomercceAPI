package com.example.ecommerce.products.services;

import com.example.ecommerce.categories.model.Category;
import com.example.ecommerce.categories.service.CategoryService;
import com.example.ecommerce.logger.annotations.LogDeleteEntityEvent;
import com.example.ecommerce.logger.annotations.LogProductEvent;
import com.example.ecommerce.products.DTOs.CreateProductDTO;
import com.example.ecommerce.products.DTOs.ProductDetailsDTO;
import com.example.ecommerce.products.DTOs.ProductListDTO;
import com.example.ecommerce.products.DTOs.SearchProductDTO;
import com.example.ecommerce.products.DTOs.UpdateProductDTO;
import com.example.ecommerce.products.logs.ProductLogEvent;
import com.example.ecommerce.products.mappers.ProductMappers;
import com.example.ecommerce.products.model.Product;
import com.example.ecommerce.products.repositories.ProductRepository;
import com.example.ecommerce.products.specifications.ProductSpecifications;
import com.example.ecommerce.products.utils.ProductSortCriteria;
import com.example.ecommerce.shared.exceptions.AlreadyExistsException;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.shared.utils.BuildPageableUtil;
import com.example.ecommerce.shared.utils.PaginationCriteria;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
  private final ProductRepository productRepo;
  private final ProductMappers productMapper;
  private final CategoryService categoryService;

  public Product findByIdOrThrow(Long productId) {
    return productRepo
        .findById(productId)
        .orElseThrow(() -> new NotFoundException("Product with id " + productId + " not found"));
  }

  @CachePut(value = "products", key = "#p0")
  @Transactional
  @LogProductEvent(loggerName = ProductService.class, event = ProductLogEvent.PRODUCT_STOCK_UPDATE)
  public ProductDetailsDTO setStock(Long productId, Integer stock) {
    var product = findByIdOrThrow(productId);
    var previousStock = product.getStock();
    product.setStock(stock);

    productRepo.saveAndFlush(product);
    log.info(
        "Product stock updated successfully. Product ID: {}, previous stock: {}, new stock: {}",
        productId,
        previousStock,
        stock);
    return productMapper.productToDetailsDTO(product);
  }

  @Transactional
  @LogProductEvent(loggerName = ProductService.class, event = ProductLogEvent.PRODUCT_CREATED)
  public ProductDetailsDTO createProduct(CreateProductDTO dto) {
    if (productRepo.getByName(dto.name()).isPresent()) {
      throw new AlreadyExistsException("Already exists product with name: " + dto.name());
    }
    Product newProduct = productMapper.createProductDTOToEntity(dto);
    Category category = categoryService.getEntityByIdOrThrow(dto.categoryId());
    newProduct.setCategory(category);
    newProduct.setIsAvailable(true);
    productRepo.saveAndFlush(newProduct);
    log.info(
        "Product created successfully. Product ID: {}, name: '{}', category ID: {}, price: {}, initial stock: {}",
        newProduct.getId(),
        newProduct.getName(),
        category.getId(),
        newProduct.getPrice(),
        newProduct.getStock());
    return productMapper.productToDetailsDTO(newProduct);
  }

  @Cacheable(value = "products", key = "#p0")
  public ProductDetailsDTO findByIdAndReturnProductDetailsDto(Long id) {
    Product product = findByIdOrThrow(id);
    return productMapper.productToDetailsDTO(product);
  }

  public Page<ProductListDTO> getAllProducts(
      PaginationCriteria<ProductSortCriteria> paginationCriteria) {

    Pageable pageable = BuildPageableUtil.build(paginationCriteria);

    return productRepo.findAll(pageable).map(productMapper::productToProductListDTO);
  }

  @Transactional
  @CacheEvict(value = "products", key = "#p0")
  @LogDeleteEntityEvent(event = "PRODUCT_DELETED", loggerName = ProductService.class)
  public void deleteProduct(Long id) {
    Product product = findByIdOrThrow(id);
    productRepo.delete(product);
    log.info(
        "Product deleted successfully. Product ID: {}, name: '{}', category ID: {}",
        product.getId(),
        product.getName(),
        product.getCategory().getId());
  }

  @Transactional
  @CachePut(value = "products", key = "#p1")
  @LogProductEvent(loggerName = ProductService.class, event = ProductLogEvent.PRODUCT_UPDATED)
  public ProductDetailsDTO updateProduct(UpdateProductDTO dto, Long id) {
    if (productRepo.getByName(dto.name()).isPresent()) {
      throw new AlreadyExistsException("Product already exists");
    }
    Product updatedProduct = productMapper.updateEntity(dto, findByIdOrThrow(id));

    productRepo.saveAndFlush(updatedProduct);
    log.info(
        "Product updated successfully. Product ID: {}, name: '{}'",
        updatedProduct.getId(),
        updatedProduct.getName());

    return productMapper.productToDetailsDTO(updatedProduct);
  }

  @Transactional
  @CachePut(value = "products", key = "#p1")
  public ProductDetailsDTO updateCategory(Long categoryId, Long productId) {
    Category category = categoryService.getEntityByIdOrThrow(categoryId);

    Product product = findByIdOrThrow(productId);

    product.setCategory(category);

    productRepo.save(product);

    log.info(
        "Product category updated successfully. Product ID: {} , new category: {}",
        product.getId(),
        category);

    return productMapper.productToDetailsDTO(product);
  }

  public Page<ProductListDTO> searchProducts(SearchProductDTO dto) {

    Specification<Product> specification =
        ProductSpecifications.hasName(dto.getName())
            .and(ProductSpecifications.filterByMaxPrice(dto.getMaxPrice()))
            .and(ProductSpecifications.filterByMinPrice(dto.getMinPrice()));

    if (dto.getCategoryId() != null) {
      Category category = categoryService.getEntityByIdOrThrow(dto.getCategoryId());
      specification = specification.and(ProductSpecifications.filterByCategory(category));
    }
    Page<Product> products =
        productRepo.findAll(specification, BuildPageableUtil.build(dto.getPaginationCriteria()));

    return products.map((productMapper::productToProductListDTO));
  }
}
