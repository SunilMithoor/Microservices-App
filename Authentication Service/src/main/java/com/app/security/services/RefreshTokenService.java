package com.app.security.services;

import com.app.model.entity.RefreshToken;

import java.util.Optional;


public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);
}