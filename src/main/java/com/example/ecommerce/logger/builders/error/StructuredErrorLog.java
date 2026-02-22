package com.example.ecommerce.logger.builders.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StructuredErrorLog {
  private String exceptionType;
  private String message;
  private String stackTrace;
  private String dateTime;
}
