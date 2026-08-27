package com.nexzus.gestiongastos.service.implementation;

import com.nexzus.gestiongastos.config.Mapper;
import com.nexzus.gestiongastos.dto.request.CreateUser;
import com.nexzus.gestiongastos.dto.request.LoginRequest;
import com.nexzus.gestiongastos.dto.response.AuthResponse;
import com.nexzus.gestiongastos.exception.BadRequestException;
import com.nexzus.gestiongastos.exception.DuplicateResourceException;
import com.nexzus.gestiongastos.model.RefreshToken;
import com.nexzus.gestiongastos.model.User;
import com.nexzus.gestiongastos.model.enums.AuthProvider;
import com.nexzus.gestiongastos.repository.RefreshTokenRepository;
import com.nexzus.gestiongastos.repository.UserRepository;
import com.nexzus.gestiongastos.security.jwt.JwtUtils;
import com.nexzus.gestiongastos.service.abstraction.IRefreshTokenService;
import com.nexzus.gestiongastos.service.abstraction.IAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final PasswordEncoder encoder;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtService;
    private final IRefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public AuthResponse create(CreateUser user) {
        if (userRepository.existsUserByEmail(user.email())) {
            log.info("Intento de crear un usuario con email ya existente: {}", user.email());
            throw new DuplicateResourceException("Usuario", "email", user.email());
        }

        if (!user.password().equals(user.confirmPassword())){
            throw new BadRequestException("Las contraseñas no coinciden");
        }

        User newUser = mapper.toEntity(user);
        newUser.setProvider(AuthProvider.LOCAL);
        newUser.setPassword(encoder.encode(user.password()));
        log.info("Usuario creado con éxito: {}", newUser);
        userRepository.save(newUser);

        return generateTokenForUser(newUser);
    }

    @Override
    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(()-> new BadCredentialsException("Credenciales incorrectas"));

        if (!encoder.matches(request.password(), user.getPassword())){
            throw new BadCredentialsException("Credenciales incorrectas");
        }
        return generateTokenForUser(user);
    }

    @Override
    public void logout(String token) {
        if (token != null) {
            refreshTokenService.revokeToken(token);
        }
    }

    @Override
    public AuthResponse refreshToken(String requestRefreshToken) {
        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    refreshTokenService.revokeToken(requestRefreshToken);
                    return generateTokenForUser(user);
                })
                .orElseThrow(() -> new BadCredentialsException("RefreshToken no encontrado o inválido"));
    }

    private AuthResponse generateTokenForUser(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtService.generateToken(userDetails);

        RefreshToken rf = refreshTokenService.create(user.getId());

        return new AuthResponse(accessToken, rf.getToken(), jwtService.getAccesTokenExpiration(), mapper.toUserResponse(user));
    }
}
