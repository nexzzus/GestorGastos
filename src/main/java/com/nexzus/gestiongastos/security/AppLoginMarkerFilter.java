package com.nexzus.gestiongastos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AppLoginMarkerFilter extends OncePerRequestFilter {
    public static final String COOKIE_NAME = "oauth_client";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/oauth2/authorization/")
                && "app".equals(request.getParameter("client"))) {
            ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, "app")
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(300)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        chain.doFilter(request, response);
    }
}
