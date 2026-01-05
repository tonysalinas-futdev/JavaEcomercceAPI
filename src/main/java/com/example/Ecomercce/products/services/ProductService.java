package com.example.Ecomercce.products.services;

import com.example.Ecomercce.categories.model.Category;
import com.example.Ecomercce.categories.service.CategoryService;
import com.example.Ecomercce.logging.service.LoggerService;
import com.example.Ecomercce.products.DTOs.CreateProductDTO;
import com.example.Ecomercce.products.DTOs.ProductDetailsDTO;
import com.example.Ecomercce.products.DTOs.ProductListDTO;
import com.example.Ecomercce.products.DTOs.SearchProductDTO;
import com.example.Ecomercce.products.DTOs.UpdateProduct;
import com.example.Ecomercce.products.mappers.ProductMappers;
import com.example.Ecomercce.products.model.Product;
import com.example.Ecomercce.products.repositories.ProductRepository;
import com.example.Ecomercce.products.specifications.ProductSpecifications;
import com.example.Ecomercce.shared.DTOs.paginatedDtos.PaginatedResponseDTO;
import com.example.Ecomercce.shared.exceptions.AlreadyExistsException;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import com.example.Ecomercce.shared.exceptions.PersistenceErrorException;
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
  private final LoggerService logger;

  public Product getProductEntityById(Long productId) {
    return productRepo
        .findById(productId)
        .orElseThrow(() -> new NotFoundException("No se ha podido encontrar el producto"));
  }

  @Transactional
  public ProductDetailsDTO createProduct(CreateProductDTO dto) {
    if (productRepo.getByName(dto.getName()).isPresent()) {
      throw new AlreadyExistsException("Ya existe un producto con el nombre: " + dto.getName());
    }

    Product newProduct = productMapper.createProductDTOToEntity(dto);

    Category category = categoryService.getCategoryEntityById(dto.getCategoryId());

    newProduct.setCategory(category);

    try {
      productRepo.saveAndFlush(newProduct);

      logger.addTypeOfLog("bussiness");
      logger.createBusinnessEventLog(
          "product_created", "createProduct", "product_id", newProduct.getId());
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }

    return productMapper.productToDetailsDTO(newProduct);
  }

  public ProductDetailsDTO getProductById(Long id) {
    Product product = getProductEntityById(id);
    return productMapper.productToDetailsDTO(product);
  }

  public PaginatedResponseDTO<ProductListDTO> getAllProducts(Integer page, Integer size) {
    if (page == null) {
      page = 0;
    }

    if (size == null) {
      size = 10;
    }

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
  public void deleteProduct(Long id) {
    Product product = getProductEntityById(id);
    try {
      productRepo.delete(product);
      logger.addTypeOfLog("bussiness");
      logger.createBusinnessEventLog("product_deleted", "deleteProduct", "product_id", id);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }
  }

  @Transactional
  public ProductDetailsDTO updateProduct(UpdateProduct dto, Long productId)
      throws NotFoundException, PersistenceErrorException {
    if (productRepo.getByName(dto.getName()).isPresent()) {
      throw new AlreadyExistsException("Ya existe un producto con ese nombre");
      
    }
    Product updatedProduct = productMapper.updateEntity(dto, getProductEntityById(productId));
    try {
      productRepo.save(updatedProduct);
      logger.addTypeOfLog("bussiness");
      logger.createBusinnessEventLog("product_updated", "updateProduct", "product_id", productId);

    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
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
      logger.addTypeOfLog("bussiness");
      logger.createBusinnessEventLog(
          "product_category_update", "updateCategory", "product_id", productId);

    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
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
