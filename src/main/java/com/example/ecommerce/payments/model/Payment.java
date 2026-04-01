package com.example.ecommerce.payments.model;

import com.example.ecommerce.payments.status.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Payments")
@Entity
@Getter
@Setter
@Builder
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_id")
  private Long orderId;
  @Column(name = "user_id")
  private Long userId;
  
  private Double amount;

  private String currency;

  @Column(unique = true, name = "payment_intent_id")
  private String paymentIntentId;
  @Enumerated(EnumType.STRING)
  private PaymentStatus status;
}
