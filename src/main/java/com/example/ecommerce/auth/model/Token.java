package com.example.ecommerce.auth.model;

import com.example.ecommerce.auth.token_enum.TokenEnum;
import com.example.ecommerce.users.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity()
@Builder
@Table(name = "token")
@NoArgsConstructor
@AllArgsConstructor
public class Token {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(unique = true, columnDefinition = "TEXT")
  private String token;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private TokenEnum type = TokenEnum.BEARER;

  private boolean revoked;

  private boolean expired;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  public User user;
}
