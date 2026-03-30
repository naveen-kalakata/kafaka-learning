package com.example.kafkalearning.auth.web;

import com.example.kafkalearning.auth.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<> register(@Valid @RequestBody RegisterRequest request) {
        log.info("HTTP POST /api/auth/register username={} email={}", request.username(), request.email());
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthTokenResponse login(@Valid @RequestBody LoginRequest request) {
        log.info("HTTP POST /api/auth/login username={}", request.username());
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthTokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        log.info("HTTP POST /api/auth/refresh");
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(NO_CONTENT)
    public void logout(@Valid @RequestBody LogoutRequest request) {
        log.info("HTTP POST /api/auth/logout");
        authService.logout(request);
    }

    @GetMapping("/me")
    public CurrentUserResponse me(@AuthenticationPrincipal Jwt jwt) {
        log.info("HTTP GET /api/auth/me subject={}", jwt.getSubject());
        return new CurrentUserResponse(
                jwt.getSubject(),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsStringList("roles").get(0)
        );
    }

    @GetMapping("/admin/hello")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminMessageResponse adminHello(@AuthenticationPrincipal Jwt jwt) {
        log.info("HTTP GET /api/auth/admin/hello subject={}", jwt.getSubject());
        return new AdminMessageResponse(
                "You have accessed an admin-only endpoint",
                jwt.getSubject()
        );
    }
}
