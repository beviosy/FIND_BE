package com.capstonedk.Maven.model.request;

import lombok.Data;

@Data
public class UserCreationRequest {
    private Long userId;
    private String loginId;
    private String password;
    private String nickname;
    private String email;
}
