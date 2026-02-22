package com.example.ecommerce.shared.utils;

import com.example.ecommerce.logger.annotations.LogMethodParams;
import org.springframework.stereotype.Component;

@Component
public class Prueba {
  @LogMethodParams
  public Integer sum(Integer a, Integer b) {
    return a + b;
  }
}
