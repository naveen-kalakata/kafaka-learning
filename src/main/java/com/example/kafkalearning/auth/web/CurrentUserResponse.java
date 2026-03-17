package com.example.kafkalearning.auth.web;

public record CurrentUserResponse(
        String username,
        String email,
        String role
) {
}
