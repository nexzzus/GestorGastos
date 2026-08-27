package com.nexzus.gestiongastos.service.implementation;

import com.nexzus.gestiongastos.exception.ResourceNotFoundException;
import com.nexzus.gestiongastos.model.RefreshToken;
import com.nexzus.gestiongastos.model.User;
import com.nexzus.gestiongastos.repository.RefreshTokenRepository;
import com.nexzus.gestiongastos.repository.UserRepository;
import com.nexzus.gestiongastos.service.abstraction.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshService implements IRefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;

    @Override
    public RefreshToken create(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .revoked(false)
                .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(!token.isValid()){
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            throw new ResourceNotFoundException("RefreshToken", "token", token.getToken());
        }
        return token;
    }

    @Override
    public void revokeToken(String tokenString) {
        refreshTokenRepository.findByToken(tokenString).ifPresent(token->{
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    @Override
    public void revokedAllUserTokens(UUID userId) {
        userRepository.findById(userId).ifPresent(refreshTokenRepository::revokeAllUserTokens);
    }
}
