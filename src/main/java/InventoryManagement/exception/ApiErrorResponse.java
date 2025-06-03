package InventoryManagement.exception;

import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ApiErrorResponse {
    private boolean success = false;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private List<String> errors;

    public ApiErrorResponse(String message, int status, List<String> errors) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    // Getters and setters
}

