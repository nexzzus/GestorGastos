package com.nexzus.gestiongastos.security;

import com.nexzus.gestiongastos.model.User;
import com.nexzus.gestiongastos.model.enums.AuthProvider;
import com.nexzus.gestiongastos.repository.UserRepository;
import com.nexzus.gestiongastos.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final com.nexzus.gestiongastos.service.abstraction.IRefreshTokenService refreshTokenService;

    private static final long REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60;


    @org.springframework.beans.factory.annotation.Value("${FRONTEND_URL:http://localhost:4200}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String firstName = oauthUser.getAttribute("given_name");
        String lastName = oauthUser.getAttribute("family_name");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User created = User.builder()
                            .email(email)
                            .firstName(firstName)
                            .lastName(lastName)
                            .provider(AuthProvider.GOOGLE)
                            .build();
                    return userRepository.save(created);
                });

        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        "",
                        List.of()
                );

        String accessToken = jwtUtils.generateToken(userDetails, user.getId());

        // Create refresh token and set HttpOnly cookie
        var refreshToken = refreshTokenService.create(user.getId());
        ResponseCookie cookie = jwtUtils.buildRefreshTokenCookie(refreshToken.getToken(), REFRESH_TOKEN_MAX_AGE);
        response.addHeader("Set-Cookie", cookie.toString());

        boolean fromApp = request.getCookies() != null
                && java.util.Arrays.stream(request.getCookies())
                .anyMatch(c -> AppLoginMarkerFilter.COOKIE_NAME.equals(c.getName())
                        && "app".equals(c.getValue()));

        String targetUrl;
        if (fromApp) {
            // Borrar la cookie marcadora
            ResponseCookie clear = ResponseCookie.from(AppLoginMarkerFilter.COOKIE_NAME, "")
                    .httpOnly(true).secure(true).sameSite("Lax").path("/").maxAge(0).build();
            response.addHeader("Set-Cookie", clear.toString());
            targetUrl = "com.mateo.gestorgastos://auth/callback?token=" + accessToken;
        } else {
            targetUrl = frontendUrl + "/auth/callback?token=" + accessToken;
        }
        response.sendRedirect(targetUrl);
    }
}