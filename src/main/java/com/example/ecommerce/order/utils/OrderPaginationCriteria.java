package com.example.ecommerce.order.utils;

import com.example.ecommerce.shared.utils.SortCriterion;
import lombok.Getter;

@Getter
public enum OrderPaginationCriteria implements SortCriterion {
  ID("id"),
  CREATED_AT("createdAt"),
  STATUS("status"),
  TOTAL_AMOUNT("totalAmount");

  private final String attributeName;

  OrderPaginationCriteria(String attributeName) {
    this.attributeName = attributeName;
  }

  @Override
  public String getDefaultValue() {
    return ID.toString();
  }
}
