package com.example.ecommerce.payments.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "webhook_event")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebHookEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "webhook_id")
  private String webhookId;

  @Column(name = "event_type")
  private String eventType;
  @Column(name = "created_at")
  @CreationTimestamp private LocalDateTime createdAt;
}
