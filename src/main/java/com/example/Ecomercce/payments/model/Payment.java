package com.example.Ecomercce.payments.model;

import com.example.Ecomercce.payments.status.PaymentStatus;
import jakarta.persistence.Entity;
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

  private Long orderId;

  private Long userId;

  private Double amount;

  private String currency;

  private String paymentIntentId;

  private PaymentStatus status;
}
