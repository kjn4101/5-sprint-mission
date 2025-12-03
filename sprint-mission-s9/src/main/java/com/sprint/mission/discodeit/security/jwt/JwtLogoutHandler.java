package com.sprint.mission.discodeit.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        Cookie expiredCookie = jwtTokenProvider.generateRefreshTokenExpirationCookie();
        response.addCookie(expiredCookie);
        
        if (authentication != null && authentication.getName() != null) {
            log.info("User logged out: {}", authentication.getName());
        } else {
            log.debug("Logout handler executed (no authenticated user)");
        }
    }
}