package com.example.auth_service.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.auth_service.domain.user.vo.Email;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(Email email);
    User save(User user);
    Optional<User> findById(UUID id);
    Page<User> findAll(Pageable pageable);
}
