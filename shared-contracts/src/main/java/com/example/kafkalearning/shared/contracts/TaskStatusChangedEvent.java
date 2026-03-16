package com.example.kafkalearning.shared.contracts;

import java.time.Instant;
import java.util.UUID;

public record TaskStatusChangedEvent(
        UUID eventId,
        String eventType,
        String aggregateType,
        UUID aggregateId,
        String previousStatus,
        String newStatus,
        String actor,
        Instant occurredAt
) {
}
