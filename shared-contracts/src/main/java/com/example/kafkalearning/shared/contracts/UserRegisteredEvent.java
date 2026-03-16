package com.example.kafkalearning.shared.contracts;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID eventId,
        String eventType,
        String aggregateType,
        UUID aggregateId,
        String username,
        String email,
        List<String> roles,
        Instant occurredAt
) {
}
