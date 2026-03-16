package com.example.kafkalearning.shared.contracts;

import java.time.Instant;
import java.util.UUID;

public record TaskCreatedEvent(
        UUID eventId,
        String eventType,
        String aggregateType,
        UUID aggregateId,
        String title,
        String description,
        String ownerUsername,
        String status,
        Instant occurredAt
) {
}
