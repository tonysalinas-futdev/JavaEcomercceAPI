package com.example.ecommerce.payments.repository;

import com.example.ecommerce.payments.model.WebHookEvent;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebHookRepository extends JpaRepository<WebHookEvent, Long> {
  Optional<WebHookEvent> findBywebhookId(String webhookId);
}
