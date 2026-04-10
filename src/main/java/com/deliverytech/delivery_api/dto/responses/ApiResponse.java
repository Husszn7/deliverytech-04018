package com.deliverytech.delivery_api.dto.responses;

import java.time.LocalDateTime;

public record ApiResponse<T>(T data, LocalDateTime timestamp ) {
    public ApiResponse(T data){
        this(data, LocalDateTime.now());
    }
}
