package com.example.ecommerce.logger.builders.audit;

import com.example.ecommerce.order.logs.events.OrderDetailsLogEvents;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class StructuredOrderDetailsEventLogWriter {
  private OrderDetailsLogEvents event;
  private Long productId;
  private Integer quantity;
  private Long orderId;
}
