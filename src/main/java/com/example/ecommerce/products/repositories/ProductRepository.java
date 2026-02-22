package com.example.ecommerce.products.repositories;

import com.example.ecommerce.products.model.Product;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository
    extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

  @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
  Page<Product> filterByName(@Param("name") String name, Pageable pageable);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Product p WHERE p.id = :productId")
  Optional<Product> findByIdForUpdate(@Param("productId") Long productId);

  @Query("SELECT p FROM Product p WHERE p.name = :name")
  Optional<Product> getByName(@Param("name") String name);

  @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.category.id= :categoryId")
  Page<Product> filterByCategory(@Param("categoryId") Integer categoryId, Pageable pageable);
}
