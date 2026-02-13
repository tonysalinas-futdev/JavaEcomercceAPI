package com.example.Ecomercce.users.models;

import com.example.Ecomercce.auth.model.Token;
import com.example.Ecomercce.cart.models.Cart;
import com.example.Ecomercce.order.models.Order;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "joined_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "is_enabled")
  private Boolean isEnabled;

  @Column(name = "account_no_locked")
  private Boolean accountNoLocked;

  @Column(name = "credentials_no_expired")
  private Boolean credentialsNoExpired;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "cart_id", unique = true)
  private Cart cart;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id")
  private Role role;

  @OneToMany(mappedBy = "user")
  @Builder.Default
  private List<Order> orders = new ArrayList<>();

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
  @Builder.Default
  private List<Token> tokens = new ArrayList<>();
}
