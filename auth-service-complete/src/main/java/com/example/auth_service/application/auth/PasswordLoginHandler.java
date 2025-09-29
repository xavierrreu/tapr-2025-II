package com.example.auth_service.application.auth;

import com.example.auth_service.application.ports.PasswordHasher;
import com.example.auth_service.application.ports.TokenService;
import com.example.auth_service.domain.user.User;
import com.example.auth_service.domain.user.UserRepository;
import com.example.auth_service.domain.user.vo.Email;
import com.example.auth_service.interfaces.rest.dto.auth.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordLoginHandler {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public TokenResponse handle(String emailStr, String pwRaw) {
        Optional<User> userOptional = userRepository.findByEmail(Email.of(emailStr));
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credencial invalido");
        }

        User user = userOptional.get();
        if (!passwordHasher.match(pwRaw, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credencial invalido");
        }

        TokenService.TokenPair pair = tokenService.issue(user);
        // create refresh token and persist
        String refreshToken = refreshTokenService.createAndPersist(user);

        return new TokenResponse(pair.token(), refreshToken, pair.expiresIn());
    }
}