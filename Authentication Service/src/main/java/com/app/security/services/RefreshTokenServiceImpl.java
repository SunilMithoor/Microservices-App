package com.app.security.services;

import com.app.exception.custom.TokenRefreshException;
import com.app.model.entity.RefreshToken;
import com.app.model.entity.User;
import com.app.repository.RefreshTokenRepository;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${security.jwt.refresh-expiration-ms}")
    private Long refreshTokenDurationMs;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                throw new TokenRefreshException("", "User not found with id: " + userId);
            }
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(user.get());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        } catch (Exception e) {
            throw new TokenRefreshException("", "Unable to create refresh token");
        }
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public void deleteToken(RefreshToken token) {
        try {
            refreshTokenRepository.delete(token);
        } catch (Exception e) {
            throw new TokenRefreshException("", "Unable to delete token");
        }
    }


    @Transactional
    public int deleteByUserId(Long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                throw new TokenRefreshException("", "User not found with id: " + userId);
            }
            return refreshTokenRepository.deleteByUser(user.get());
        } catch (Exception e) {
            throw new TokenRefreshException("", "Unable to delete refresh tokens for user id: " + userId);
        }
    }
}
