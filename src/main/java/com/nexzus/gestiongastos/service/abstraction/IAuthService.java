package com.nexzus.gestiongastos.service.abstraction;

import com.nexzus.gestiongastos.dto.request.CreateUser;
import com.nexzus.gestiongastos.dto.request.LoginRequest;
import com.nexzus.gestiongastos.dto.response.AuthResponse;

public interface IAuthService {
    AuthResponse create(CreateUser user);

    AuthResponse login(LoginRequest request);

    void logout(String token);

    AuthResponse refreshToken(String requestRefreshToken);
}
