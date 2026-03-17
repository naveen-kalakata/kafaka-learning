package com.example.kafkalearning.auth.web;

public record RegisterResponse(
        String message,
        String username,
        String email,
        String role
) {
}
