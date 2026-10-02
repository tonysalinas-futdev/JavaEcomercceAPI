package com.example.ecommerce.products.utils;

import com.example.ecommerce.shared.utils.SortCriterion;
import lombok.Getter;

@Getter
public enum ProductSortCriteria implements SortCriterion {
  ID("id"),
  PRICE("price"),
  STOCK("stock"),
  CREATED_AT("createdAt"),
  CATEGORY("category");

  private final String attributeName;

  ProductSortCriteria(String attributeName) {
    this.attributeName = attributeName;
  }

  @Override
  public String getDefaultValue() {
    return ID.toString();
  }

  @Override
  public String getAttributeName() {
    return attributeName;
  }
}
