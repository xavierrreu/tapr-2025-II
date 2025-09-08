package com.example.auth_service.domain.refresh.vo;

import lombok.Getter;

@Getter
public class TokenHash {
    private final String value;

    public TokenHash(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Token hash cannot be blank");
        this.value = value;
    }
}