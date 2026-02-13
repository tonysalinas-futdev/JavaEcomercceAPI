package com.example.Ecomercce.payments.repository;

import com.example.Ecomercce.payments.model.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Optional<Payment> findByPaymentIntentId(String paymentIntentId);

  Optional<Payment> findByOrderId(Long id);
}
