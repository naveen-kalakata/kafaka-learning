package com.example.kafkalearning.auth.web;

public record AdminMessageResponse(
        String message,
        String requestedBy
) {
}
