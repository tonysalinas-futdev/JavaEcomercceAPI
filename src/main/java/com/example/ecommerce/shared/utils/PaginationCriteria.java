package com.example.ecommerce.shared.utils;

import com.example.ecommerce.shared.exceptions.InvalidRequestException;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PaginationCriteria<T extends SortCriterion> {
  private Integer page = 0;
  private Integer items = 10;
  private T sortCriterion;
  private Boolean ascending = true;

  public PaginationCriteria(Integer page, Integer items, T sortCriterion, Boolean ascending) {
    if (items == 0) {
      throw new InvalidRequestException("The minimum of  items is 1");
    }
    this.page = page;
    this.items = items;
    this.sortCriterion = sortCriterion;
    this.ascending = ascending;
  }
}
