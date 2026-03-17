package com.example.kafkalearning.auth.service;

import com.example.kafkalearning.auth.domain.AppUser;
import com.example.kafkalearning.auth.events.KafkaTopics;
import com.example.kafkalearning.auth.events.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AuthEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AuthEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserRegistered(AppUser user) {
        UserRegisteredEvent event = new UserRegisteredEvent(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                Instant.now()
        );

        log.info("Publishing user-registered event for userId={} to topic={}", user.getId(), KafkaTopics.USER_REGISTERED);
        try {
            kafkaTemplate.send(KafkaTopics.USER_REGISTERED, user.getId().toString(), event);
        } catch (Exception exception) {
            log.error("Kafka publish failed for userId={} topic={}", user.getId(), KafkaTopics.USER_REGISTERED, exception);
            throw exception;
        }
    }
}
