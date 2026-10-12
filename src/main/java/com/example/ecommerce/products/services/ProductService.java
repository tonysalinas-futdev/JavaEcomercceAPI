package com.example.ecommerce.products.services;

import com.example.ecommerce.categories.model.Category;
import com.example.ecommerce.categories.service.CategoryService;
import com.example.ecommerce.products.DTOs.CreateProductDTO;
import com.example.ecommerce.products.DTOs.ProductDetailsDTO;
import com.example.ecommerce.products.DTOs.ProductListDTO;
import com.example.ecommerce.products.DTOs.SearchProductDTO;
import com.example.ecommerce.products.DTOs.UpdateProductDTO;
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
  public ProductDetailsDTO setStock(Long productId, Integer stock) {
    var product = findByIdOrThrow(productId);
    var previousStock = product.getStock();
    product.setStock(stock);

    productRepo.saveAndFlush(product);
    log.atInfo()
        .addKeyValue("event_type", "product_stock_updated")
        .addKeyValue("product_id", productId)
        .addKeyValue("previous_stock", previousStock)
        .addKeyValue("new stock", stock)
        .log("Product stock updated successfully");

    return productMapper.productToDetailsDTO(product);
  }

  @Transactional
  public ProductDetailsDTO createProduct(CreateProductDTO dto) {
    if (productRepo.getByName(dto.name()).isPresent()) {
      throw new AlreadyExistsException("Already exists product with name: " + dto.name());
    }
    Product newProduct = productMapper.createProductDTOToEntity(dto);
    Category category = categoryService.getEntityByIdOrThrow(dto.categoryId());
    newProduct.setCategory(category);
    newProduct.setIsAvailable(true);
    productRepo.saveAndFlush(newProduct);

    log.atInfo()
        .addKeyValue("event_type", "product_created")
        .addKeyValue("product_id", newProduct.getId())
        .addKeyValue("product_name", newProduct.getName())
        .addKeyValue("category_id", category.getId())
        .addKeyValue("price", newProduct.getPrice())
        .addKeyValue("stock", newProduct.getStock())
        .log("Product created successfully");

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
  public ProductDetailsDTO updateProduct(UpdateProductDTO dto, Long id) {
    if (productRepo.getByName(dto.name()).isPresent()) {
      throw new AlreadyExistsException("Product already exists");
    }
    Product updatedProduct = productMapper.updateEntity(dto, findByIdOrThrow(id));

    productRepo.saveAndFlush(updatedProduct);
    log.atInfo()
        .addKeyValue("event_type", "product_updated")
        .addKeyValue("product_id", updatedProduct.getId())
        .log("Product updated successfully");

    return productMapper.productToDetailsDTO(updatedProduct);
  }

  @Transactional
  @CachePut(value = "products", key = "#p1")
  public ProductDetailsDTO updateCategory(Long categoryId, Long productId) {
    Category category = categoryService.getEntityByIdOrThrow(categoryId);

    Product product = findByIdOrThrow(productId);
    Long previousCategoryId = product.getCategory().getId();
    product.setCategory(category);

    productRepo.save(product);
    log.atInfo()
        .addKeyValue("event_type", "product_category_updated")
        .addKeyValue("product_id", product.getId())
        .addKeyValue("previous_category_id", previousCategoryId)
        .addKeyValue("new_category_id", product.getCategory().getId())
        .log("Product category updated successfully");

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
