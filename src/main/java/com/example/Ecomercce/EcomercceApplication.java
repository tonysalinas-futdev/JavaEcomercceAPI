package com.example.Ecomercce;

import com.example.Ecomercce.categories.model.Category;
import com.example.Ecomercce.categories.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcomercceApplication {

  public static void main(String[] args) {
    SpringApplication.run(EcomercceApplication.class, args);
  }

  @Bean
  CommandLineRunner init(CategoryRepository repo) {
    return args -> {
      Category category = Category.builder().name("Ropa").description("Ropa para comprar").build();

      repo.save(category);
    };
  }
}
