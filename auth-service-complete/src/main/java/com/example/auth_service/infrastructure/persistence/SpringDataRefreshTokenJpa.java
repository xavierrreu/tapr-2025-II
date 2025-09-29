package com.example.auth_service.infrastructure.persistence;

import com.example.auth_service.domain.refresh.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataRefreshTokenJpa extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenHashAndRevokedFalseAndExpiresAtAfter(String tokenHash, Instant now);
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}