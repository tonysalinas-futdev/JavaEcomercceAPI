package com.example.ecommerce.auth.model;

import com.example.ecommerce.auth.token_enum.TokenEnum;
import com.example.ecommerce.users.models.User;
import jakarta.persistence.*;

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


  @OneToOne(fetch = FetchType.EAGER, mappedBy = "token")
  public User user;
}
