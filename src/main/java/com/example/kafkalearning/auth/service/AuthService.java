package com.example.kafkalearning.auth.service;

import com.example.kafkalearning.auth.domain.AppUser;
import com.example.kafkalearning.auth.domain.RefreshToken;
import com.example.kafkalearning.auth.domain.UserRole;
import com.example.kafkalearning.auth.repository.RefreshTokenRepository;
import com.example.kafkalearning.auth.repository.UserRepository;
import com.example.kafkalearning.auth.web.AuthTokenResponse;
import com.example.kafkalearning.auth.web.LoginRequest;
import com.example.kafkalearning.auth.web.LogoutRequest;
import com.example.kafkalearning.auth.web.RefreshRequest;
import com.example.kafkalearning.auth.web.RegisterRequest;
import com.example.kafkalearning.auth.web.RegisterResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthEventPublisher authEventPublisher;
    private final long refreshExpirationMinutes;

    public AuthService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthEventPublisher authEventPublisher,
            @Value("${auth.jwt.refresh-expiration-minutes}") long refreshExpirationMinutes
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authEventPublisher = authEventPublisher;
        this.refreshExpirationMinutes = refreshExpirationMinutes;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        log.info("Register request received for username={} email={}", request.username(), request.email());

        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(CONFLICT, "Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(CONFLICT, "Email already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.USER);

        AppUser savedUser = userRepository.save(user);
        log.info("User persisted with id={} username={}", savedUser.getId(), savedUser.getUsername());
        authEventPublisher.publishUserRegistered(savedUser);

        return new RegisterResponse(
                "User registered successfully",
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    @Transactional
    public AuthTokenResponse login(LoginRequest request) {
        log.info("Login request received for username={}", request.username());
        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid username or password");
        }

        log.info("Login successful for userId={} username={}", user.getId(), user.getUsername());
        return createTokenResponse(user);
    }

    @Transactional
    public AuthTokenResponse refresh(RefreshRequest request) {
        log.info("Refresh request received");
        RefreshToken storedToken = refreshTokenRepository.findByTokenAndRevokedFalse(request.refreshToken())
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Refresh token is invalid"));

        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            storedToken.setRevoked(true);
            throw new ResponseStatusException(UNAUTHORIZED, "Refresh token has expired");
        }

        storedToken.setRevoked(true);
        log.info("Refresh token accepted for userId={}", storedToken.getUser().getId());
        return createTokenResponse(storedToken.getUser());
    }

    @Transactional
    public void logout(LogoutRequest request) {
        log.info("Logout request received");
        refreshTokenRepository.findByTokenAndRevokedFalse(request.refreshToken())
                .ifPresent(token -> {
                    token.setRevoked(true);
                    log.info("Refresh token revoked for userId={}", token.getUser().getId());
                });
    }

    private AuthTokenResponse createTokenResponse(AppUser user) {
        String accessToken = jwtService.generateToken(user);
        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setExpiresAt(Instant.now().plus(refreshExpirationMinutes, ChronoUnit.MINUTES));
        refreshToken.setRevoked(false);
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);
        log.info("Issued token pair for userId={} username={} accessExpiresInMinutes={}",
                user.getId(), user.getUsername(), jwtService.getExpirationMinutes());

        return new AuthTokenResponse(
                accessToken,
                refreshTokenValue,
                "Bearer",
                jwtService.getExpirationMinutes()
        );
    }
}
