package com.example.Ecomercce.shared.dtos.paginatedresponse;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponseDTO<T> {
  private List<T> items;
  private Boolean hasNext;
  private Boolean hasPrevious;
  private Integer page;
  private Integer totalPages;
  private Long totalElements;
  private Integer numberOfElementsInPage;
  private Integer size;
}
