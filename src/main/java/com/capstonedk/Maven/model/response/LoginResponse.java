package com.capstonedk.Maven.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private boolean success;
    private String code;
    private Tokens result;
    private String message;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tokens {
        private String accessToken;
        private String refreshToken;
    }
}
