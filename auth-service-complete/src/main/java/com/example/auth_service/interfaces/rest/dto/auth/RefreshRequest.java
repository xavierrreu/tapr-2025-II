package com.example.auth_service.interfaces.rest.dto.auth;

import lombok.Getter;

@Getter
public class RefreshRequest {
    private String refreshToken;
}