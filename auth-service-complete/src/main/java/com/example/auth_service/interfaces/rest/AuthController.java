package com.example.auth_service.interfaces.rest;

import com.example.auth_service.application.auth.PasswordLoginHandler;
import com.example.auth_service.application.auth.RefreshTokenService;
import com.example.auth_service.application.ports.TokenService;
import com.example.auth_service.domain.refresh.RefreshToken;
import com.example.auth_service.domain.user.UserRepository;
import com.example.auth_service.interfaces.rest.dto.auth.PasswordLoginRequest;
import com.example.auth_service.interfaces.rest.dto.auth.RefreshRequest;
import com.example.auth_service.interfaces.rest.dto.auth.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final PasswordLoginHandler passwordLoginHandler;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginWithPassword(@Valid @RequestBody PasswordLoginRequest request) {
        TokenResponse token = passwordLoginHandler.handle(request.email(), request.password());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        var opt = refreshTokenService.findActiveByToken(request.getRefreshToken());
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        RefreshToken existing = opt.get();
        // load user
        var userOpt = userRepository.findById(existing.getUserId());
        if (userOpt.isEmpty()) {
            // revoke existing just in case
            refreshTokenService.revoke(existing);
            return ResponseEntity.status(401).build();
        }
        var user = userOpt.get();
        // issue new access token
        TokenService.TokenPair pair = tokenService.issue(user);
        // create and persist new refresh token, revoke old
        String newRefresh = refreshTokenService.createAndPersist(user);
        refreshTokenService.revoke(existing);
        return ResponseEntity.ok(new TokenResponse(pair.token(), newRefresh, pair.expiresIn()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshRequest request) {
        var opt = refreshTokenService.findActiveByToken(request.getRefreshToken());
        if (opt.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        RefreshToken r = opt.get();
        refreshTokenService.revoke(r);
        return ResponseEntity.noContent().build();
    }
}