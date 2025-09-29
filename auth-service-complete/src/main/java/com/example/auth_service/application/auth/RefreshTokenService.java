package com.example.auth_service.application.auth;

import com.example.auth_service.application.ports.TokenService;
import com.example.auth_service.domain.refresh.RefreshToken;
import com.example.auth_service.domain.refresh.RefreshTokenRepository;
import com.example.auth_service.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    private final RefreshTokenProperties props = new RefreshTokenProperties();

    public record RefreshTokenProperties(long ttlSeconds) {
        public RefreshTokenProperties() {
            this(60L * 60 * 24 * 30); // 30 days default
        }
    }

    public String createAndPersist(User user) {
        String raw = java.util.UUID.randomUUID().toString() + ":" + user.getId();
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
        String hash = sha256(token);
        Instant expires = Instant.now().plus(props.ttlSeconds(), ChronoUnit.SECONDS);
        RefreshToken entity = RefreshToken.createForUser(user.getId(), hash, expires);
        refreshTokenRepository.save(entity);
        return token;
    }

    public Optional<RefreshToken> findActiveByToken(String token) {
        String hash = sha256(token);
        return refreshTokenRepository.findActiveByHash(hash);
    }

    public void revoke(RefreshToken token) {
        refreshTokenRepository.revoke(token);
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}