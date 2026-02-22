package com.example.ecommerce.testServices;

import com.example.ecommerce.categories.model.Category;
import com.example.ecommerce.categories.repository.CategoryRepository;
import com.example.ecommerce.products.model.Product;
import com.example.ecommerce.products.repositories.ProductRepository;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductTestServiceHelper {
  @Autowired private ProductRepository productRepo;
  @Autowired private CategoryRepository categoryRepo;

  public Category createCategory() {
    if (categoryRepo.getByName("Muebles").isEmpty()) {
      Category category =
          Category.builder().name("Muebles").description("Muebles para la casa").pic("fsd").build();

      categoryRepo.saveAndFlush(category);
      return category;

    } else
      return categoryRepo
          .getByName("Muebles")
          .orElseThrow(() -> new NotFoundException("No se ha encontrado la categoría"));
  }

  public Product createProductWithNameEqualHelado() {
    Category category = createCategory();
    Optional<Product> product = productRepo.getByName("Helado");

    if (!product.isPresent()) {
      Product producto =
          Product.builder()
              .name("Helado")
              .description("Un rico helado")
              .price(40.0)
              .stock(50)
              .category(category)
              .build();

      productRepo.saveAndFlush(producto);
      return producto;
    }
    return product.get();
  }
}
