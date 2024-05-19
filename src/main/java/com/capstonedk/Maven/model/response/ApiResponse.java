package com.capstonedk.Maven.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    private boolean success;
    private String code;
    private String message;
    private Object result;
    private Long reviewId; // New field for review ID

    // Constructor without review ID
    public ApiResponse(boolean success, String code, String message, Object result) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
