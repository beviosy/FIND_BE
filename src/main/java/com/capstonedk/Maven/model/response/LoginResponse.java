package com.capstonedk.Maven.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String code;
    private Tokens result;
    private String message;

    @Data
    @AllArgsConstructor
    public static class Tokens {
        private String accessToken;
        private String refreshToken;
    }
}
