package com.example.Ecomercce.auth.repository;

import com.example.Ecomercce.auth.model.Token;
import com.example.Ecomercce.users.models.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
  public Optional<Token> findByToken(String token);

  @Query("SELECT t FROM Token t WHERE t.user = :user")
  public List<Token> getAllTokensFromUser(@Param("user") User user);

  @Query("SELECT t FROM Token t WHERE t.revoked=true AND t.expired=true")
  public List<Token> getAllInvalidTokens();

  @Query("SELECT t FROM Token t WHERE t.token= :token")
  public Optional<Token> getByToken(@Param("token") String token);
}
