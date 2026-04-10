package com.deliverytech.delivery_api.dto.responses;

import java.util.List;

import org.springframework.data.domain.Page;

public record PagedResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean last
) {
    public PagedResponse(Page<T> page){
        this(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isLast()
        );
    }
}
