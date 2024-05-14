package com.capstonedk.Maven.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    private String loginId;
    private String password;
}
