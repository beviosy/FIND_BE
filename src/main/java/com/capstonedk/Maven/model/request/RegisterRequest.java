package com.capstonedk.Maven.model.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String loginId;
    private String password;
    private String nickname;
}

