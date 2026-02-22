package com.example.ecommerce.users.repository;

import com.example.ecommerce.users.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  public Optional<User> findUserByName(String name);

  public Optional<User> findUserByEmail(String email);

  @Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id= :id")
  public Optional<User> findByIdAndLoadOrders(@Param("id") Long id);

  @Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.email= :email")
  public Optional<User> findByEmailAndLoadOrders(@Param("email") String email);
}
