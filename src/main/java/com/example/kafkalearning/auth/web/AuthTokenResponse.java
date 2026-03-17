package com.example.kafkalearning.auth.web;

public record AuthTokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInMinutes
) {
}
