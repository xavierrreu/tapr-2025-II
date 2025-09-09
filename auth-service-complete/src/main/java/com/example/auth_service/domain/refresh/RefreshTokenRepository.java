package com.example.auth_service.domain.refresh;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken token);
    Optional<RefreshToken> findActiveByHash(String tokenHash);
    void revoke(RefreshToken token);
    void deleteById(UUID id);
}