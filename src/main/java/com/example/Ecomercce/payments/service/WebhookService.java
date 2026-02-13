package com.example.Ecomercce.payments.service;

import com.example.Ecomercce.payments.model.WebHookEvent;
import com.example.Ecomercce.payments.repository.WebHookRepository;
import com.stripe.model.Event;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WebhookService {
  private final WebHookRepository repo;

  public Optional<WebHookEvent> getByEventId(String webhookEventId) {
    return repo.findBywebhookId(webhookEventId);
  }

  public void buildAndSaveEvent(Event event) {
    Optional<WebHookEvent> existingEvent = repo.findBywebhookId(event.getId());

    if (existingEvent.isPresent()) {
      return;
    }

    var webhookEvent =
        WebHookEvent.builder().eventType(event.getType()).webhookId(event.getId()).build();

    repo.save(webhookEvent);
  }
}
