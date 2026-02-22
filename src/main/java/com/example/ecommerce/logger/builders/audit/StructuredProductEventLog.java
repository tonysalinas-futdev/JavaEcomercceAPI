package com.example.ecommerce.logger.builders.audit;

import com.example.ecommerce.products.logs.ProductLogEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StructuredProductEventLog {
  ProductLogEvent event;
  String id;
  String name;
  Integer stock;
  Integer stockToUpdate;
}
