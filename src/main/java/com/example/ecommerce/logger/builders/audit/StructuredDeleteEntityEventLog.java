package com.example.ecommerce.logger.builders.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StructuredDeleteEntityEventLog {
  Long id;
  String event;
}
