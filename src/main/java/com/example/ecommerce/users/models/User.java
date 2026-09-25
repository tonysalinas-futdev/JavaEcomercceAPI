package com.example.ecommerce.users.models;

import com.example.ecommerce.auth.model.Token;
import com.example.ecommerce.cart.models.Cart;
import com.example.ecommerce.order.models.Order;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString()
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

  @ToString.Exclude
  @Column(nullable = false)
  private String password;

  @Column(name = "is_enabled")
  private Boolean isEnabled;

  @Column(name = "account_no_locked")
  private Boolean accountNoLocked;

  @Column(name = "credentials_no_expired")
  private Boolean credentialsNoExpired;

  @ToString.Exclude
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "cart_id", unique = true)
  private Cart cart;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "role_user",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @Builder.Default
  private List<Role> roles = new ArrayList<>();

  @ToString.Exclude
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  @Builder.Default
  private List<Order> orders = new ArrayList<>();

  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "token_id", unique = true)
  private Token token;
}
