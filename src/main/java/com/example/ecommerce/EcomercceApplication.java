package com.example.ecommerce;

import com.example.ecommerce.categories.model.Category;
import com.example.ecommerce.categories.repository.CategoryRepository;
import com.example.ecommerce.users.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy
@EnableCaching
public class EcomercceApplication {

  public static void main(String[] args) {

    SpringApplication.run(EcomercceApplication.class, args);
  }

  @Bean
  CommandLineRunner init(CategoryRepository repo, RoleRepository roleRepo) {
    log.info("Iniciando la aplicación de e-commerce");
    return args -> {
      Category category = Category.builder().name("Ropa").description("Ropa para comprar").build();
      repo.save(category);
    };
  }
}
