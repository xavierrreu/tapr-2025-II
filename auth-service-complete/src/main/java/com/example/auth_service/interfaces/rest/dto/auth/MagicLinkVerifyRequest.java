package com.example.auth_service.interfaces.rest.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record MagicLinkVerifyRequest(@NotBlank String token) {
}
