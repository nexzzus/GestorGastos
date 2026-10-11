package com.nexzus.gestiongastos.controller;

import com.nexzus.gestiongastos.dto.request.CreateUser;
import com.nexzus.gestiongastos.dto.request.LoginRequest;
import com.nexzus.gestiongastos.dto.response.AuthResponse;
import com.nexzus.gestiongastos.service.abstraction.IAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService userService;

    private static final String PATH = "/api/auth";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final long REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60;

    private ResponseCookie buildRefreshTokenCookie(String token, long maxAge) {
        return ResponseCookie.from(REFRESH_TOKEN, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path(PATH)
                .maxAge(maxAge)
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid
            @RequestBody CreateUser request,
            HttpServletResponse response) {

        AuthResponse auth = userService.create(request);

        ResponseCookie cookie = buildRefreshTokenCookie(auth.refreshToken(), REFRESH_TOKEN_MAX_AGE);
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(auth.accessToken(), null, auth.expiresIn(), auth.user()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthResponse auth = userService.login(request);

        ResponseCookie cookie = buildRefreshTokenCookie(auth.refreshToken(), REFRESH_TOKEN_MAX_AGE);
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new AuthResponse(auth.accessToken(), null, auth.expiresIn(), auth.user()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {
            refreshToken = Arrays.stream(cookies)
                    .filter(c -> REFRESH_TOKEN.equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        if (refreshToken != null) {
            userService.logout(refreshToken);
        }

        ResponseCookie cookie = buildRefreshTokenCookie("", 0);
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No cookies found in request");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> c.getName().equals(REFRESH_TOKEN))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No refresh token found"));

        AuthResponse auth = userService.refreshToken(refreshToken);

        ResponseCookie cookie = buildRefreshTokenCookie(auth.refreshToken(), REFRESH_TOKEN_MAX_AGE);
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok().body(new AuthResponse(auth.accessToken(), null, auth.expiresIn(), auth.user()));
    }

}

