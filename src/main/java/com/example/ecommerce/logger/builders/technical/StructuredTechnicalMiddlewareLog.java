package com.example.ecommerce.logger.builders.technical;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StructuredTechnicalMiddlewareLog {
  String path;
  String method;
  String duration;
  String statusCode;
  String ip;
}
