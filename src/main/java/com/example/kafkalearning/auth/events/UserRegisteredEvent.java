package com.example.kafkalearning.auth.events;

import java.time.Instant;

public record UserRegisteredEvent(
        Long userId,
        String username,
        String email,
        String role,
        Instant registeredAt
) {
}
