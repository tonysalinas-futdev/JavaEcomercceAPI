package com.example.ecommerce.logger.builders.audit;

import com.example.ecommerce.logger.annotations.LogCategoryEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StructuredCategoryEventLog {
  LogCategoryEvent event;
  Long categoryId;
  String name;
}
