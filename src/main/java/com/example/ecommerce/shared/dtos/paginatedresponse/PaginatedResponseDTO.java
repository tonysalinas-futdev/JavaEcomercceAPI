package com.example.ecommerce.shared.dtos.paginatedresponse;

import java.util.List;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Builder
public record PaginatedResponseDTO<T>(
        List<T> items,
        Boolean hasNext,
        Boolean hasPrevious,
        Integer page,
        Integer totalPages,
        Long totalElements,
        Integer numberOfElementsInPage,
        Integer size
) {


}
