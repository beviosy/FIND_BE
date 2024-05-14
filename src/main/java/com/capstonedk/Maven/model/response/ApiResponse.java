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
}
