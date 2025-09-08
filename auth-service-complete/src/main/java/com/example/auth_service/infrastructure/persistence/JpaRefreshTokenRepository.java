package com.example.auth_service.infrastructure.persistence;

import com.example.auth_service.domain.refresh.RefreshToken;
import com.example.auth_service.domain.refresh.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaRefreshTokenRepository implements RefreshTokenRepository {

    private final SpringDataRefreshTokenJpa jpa;

    @Override
    public RefreshToken save(RefreshToken token) {
        return jpa.save(token);
    }

    @Override
    public Optional<RefreshToken> findActiveByHash(String tokenHash) {
        return jpa.findByTokenHashAndRevokedFalseAndExpiresAtAfter(tokenHash, Instant.now());
    }

    @Override
    public void revoke(RefreshToken token) {
        token.revoke();
        jpa.save(token);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}