package com.example.Ecomercce;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.Ecomercce.Models.Category;
import com.example.Ecomercce.Repositories.CategoryRepository;

@SpringBootApplication
public class EcomercceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomercceApplication.class, args);}

		@Bean
		CommandLineRunner init(CategoryRepository repo){
			return args ->{
				Category category=Category
				.builder()
				.name("Ropa")
				.description("Ropa para comprar")
				.build();

				repo.save(category);

			};
		}
	
}

