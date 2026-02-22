package com.example.ecommerce.categories.repository;

import com.example.ecommerce.categories.model.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query("SELECT c FROM Category c WHERE c.name= :name")
  Optional<Category> getByName(@Param("name") String name);
}
