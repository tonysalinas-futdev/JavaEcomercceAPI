package com.example.ecommerce.shared.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class BuildPageableUtil {
  public static <T extends SortCriterion> Pageable build(PaginationCriteria<T> paginationCriteria) {
    Sort sort =
        Sort.by(
            paginationCriteria.getAscending() ? Sort.Direction.ASC : Sort.Direction.DESC,
            paginationCriteria.getSortCriterion() != null
                ? paginationCriteria.getSortCriterion().getAttributeName()
                : "id");
    return PageRequest.of(paginationCriteria.getPage(), paginationCriteria.getItems(), sort);
  }
}
