package com.example.ecommerce.users.utils;

import com.example.ecommerce.shared.utils.SortCriterion;

public enum UserSortCriteria implements SortCriterion {
  ID("id"),
  CREATED_AT("createdAt"),
  NAME("name");

  private String attributeName;

  UserSortCriteria(String attributeName) {
    this.attributeName = attributeName;
  }

  @Override
  public String getDefaultValue() {
    return ID.toString();
  }

  public String getAttributeName() {
    return attributeName;
  }
}
