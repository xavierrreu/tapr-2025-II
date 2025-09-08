package com.example.auth_service.domain.refresh.vo;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ExpiresAt {
    private final Instant value;

    public ExpiresAt(Instant value) {
        if (value == null) throw new IllegalArgumentException("expiresAt cannot be null");
        this.value = value;
    }
}