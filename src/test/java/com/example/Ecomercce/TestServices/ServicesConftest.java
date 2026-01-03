package com.example.Ecomercce.testServices;

import com.example.Ecomercce.categories.model.Category;
import com.example.Ecomercce.categories.repository.CategoryRepository;
import com.example.Ecomercce.products.model.Product;
import com.example.Ecomercce.products.repositories.ProductRepository;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import com.example.Ecomercce.shared.exceptions.PersistenceErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicesConftest {
  @Autowired private ProductRepository productRepo;

  @Autowired private CategoryRepository categoryRepo;

  public Category returnCategory() throws NotFoundException {
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

  public void registerProduct() throws NotFoundException, PersistenceErrorException {
    Category category = returnCategory();
    if (productRepo.getByName("Helado").isEmpty()) {
      Product producto =
          Product.builder()
              .name("Helado")
              .description("Un rico helado")
              .price(40.0)
              .stock(50)
              .category(category)
              .build();

      productRepo.saveAndFlush(producto);
    }
  }
}
