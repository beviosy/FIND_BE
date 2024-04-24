package com.capstonedk.Maven.model.request;

import lombok.Data;

@Data
public class UserCreationRequest {
    private Long userId;
    private String login_id;
    private String password;
    private String nickname;
    private String email;
}
