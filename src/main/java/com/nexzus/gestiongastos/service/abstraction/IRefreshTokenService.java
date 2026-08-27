package com.nexzus.gestiongastos.service.abstraction;

import com.nexzus.gestiongastos.model.RefreshToken;

import java.util.UUID;

public interface IRefreshTokenService {
    RefreshToken create(UUID userId);
    RefreshToken verifyExpiration(RefreshToken token);
    void revokeToken(String tokenString);
    void revokedAllUserTokens(UUID userId);
}
