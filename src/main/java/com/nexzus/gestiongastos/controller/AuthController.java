package com.nexzus.gestiongastos.controller;

import com.nexzus.gestiongastos.dto.request.CreateUser;
import com.nexzus.gestiongastos.dto.request.LoginRequest;
import com.nexzus.gestiongastos.dto.response.AuthResponse;
import com.nexzus.gestiongastos.exception.ResourceNotFoundException;
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

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService userService;

    private final String PATH = "/api/auth";

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid
            @RequestBody CreateUser request,
            HttpServletResponse response) {

        AuthResponse auth = userService.create(request);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", auth.refreshToken())
                .httpOnly(true)
                .secure(false)
                .path(PATH)
                .maxAge(7 * 24 * 60 * 60)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(auth.accessToken(), null, auth.expiresIn(), auth.user()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid
            @RequestBody LoginRequest request,
            HttpServletResponse response
            ){
        AuthResponse auth = userService.login(request);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", auth.accessToken())
                .httpOnly(true)
                .secure(false)
                .path(PATH)
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(auth.accessToken(), null, auth.expiresIn(), auth.user()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null){
            refreshToken = Arrays.stream(cookies)
                    .filter(c -> "refreshToken".equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        if (refreshToken != null){
            userService.logout(refreshToken);
        }

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path(PATH)
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request){
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(()-> new ResourceNotFoundException("No refresh token found"));

        return ResponseEntity.ok().body(userService.refreshToken(refreshToken));
    }
}
