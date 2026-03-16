# Kafka Learning

This repository is a learning-first enterprise simulation for:

- migrating Java 8 habits to Java 17
- moving from classic Spring MVC style to Spring Boot 4
- implementing JWT issuing and validation
- learning Kafka through real producer/consumer flows

## Architecture

- `shared-contracts`: versioned Kafka event records and shared API error model
- `auth-service`: user registration, login, refresh token persistence, RSA JWT issuing, JWKS exposure
- `task-service`: secured task CRUD, role-based authorization, Kafka event publishing
- `audit-service`: Kafka consumers, idempotent audit log persistence, read APIs for event inspection

## Local infra

`compose.yaml` starts:

- PostgreSQL with separate databases for each service
- Kafka in KRaft mode
- Kafka UI for topic inspection

## Suggested learning order

1. Read [01-foundation.md](/C:/Users/navee/OneDrive/Desktop/Kafka-Learning/docs/phases/01-foundation.md)
2. Read [02-java-17.md](/C:/Users/navee/OneDrive/Desktop/Kafka-Learning/docs/phases/02-java-17.md)
3. Build and run `auth-service`
4. Build and run `task-service`
5. Build and run `audit-service`
6. Follow the smoke flow in [06-kafka-reliability.md](/C:/Users/navee/OneDrive/Desktop/Kafka-Learning/docs/phases/06-kafka-reliability.md)

## Expected local prerequisites

- JDK 17 or newer on `PATH`
- Docker Desktop
- PowerShell or a POSIX shell

The current machine only has Java 8 available, so the codebase targets Java 17 but cannot be executed here until a newer JDK is installed.
