package com.example.ecommerce.logger.builders.audit;

import com.example.ecommerce.auth.log.events.AuthLogEvents;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StructuredAuthEventLog {
  AuthLogEvents event;
  String userEmail;
}
