package com.agora.notification.dto;

import lombok.Data;

@Data
public class RegisterTokenRequest {
    private String firebaseToken;
    private String platform;
}
