package com.deliverytech.delivery_api.exception;

import java.util.List;

public record ErrorResponse(int status, String message, long timestamp, List<ValidationError> errors) {
    
    public record ValidationError(String field, String message){}
}
