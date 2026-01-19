package com.example.Ecomercce.users.repository;

import com.example.Ecomercce.users.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  public Optional<User> findUserByName(String name);

  public Optional<User> findUserByEmail(String email);
}
