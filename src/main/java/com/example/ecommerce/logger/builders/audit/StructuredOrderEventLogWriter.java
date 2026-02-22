package com.example.ecommerce.logger.builders.audit;

import com.example.ecommerce.order.logs.events.OrderLogsEvents;
import com.example.ecommerce.order.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StructuredOrderEventLogWriter {
  private OrderLogsEvents event;
  private Long id;
  private OrderStatus status;
  private Double totalAmount;
  private Long userId;
}
