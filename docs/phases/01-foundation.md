# Phase 1: Foundation and Migration Framing

## What this phase builds

This phase replaces the single generated Spring Boot app with a Maven parent project and four focused modules. That decision matters because enterprise systems are easier to understand when infrastructure concerns, shared contracts, and service boundaries are visible from day one.

## Classes and files introduced here

- `pom.xml`
  Why: it becomes the parent build that enforces Java 17, centralizes dependency management, and makes the repo feel like a real multi-service system instead of a tutorial toy.
- `shared-contracts`
  Why: Kafka payloads are now explicit shared contracts rather than hidden JSON strings.
- `auth-service`, `task-service`, `audit-service`
  Why: JWT validation across services and Kafka-driven integration are much easier to learn when the boundaries are real.
- `compose.yaml`
  Why: local infrastructure should be reproducible, disposable, and close to what teams use in enterprise environments.

## Why this design was chosen

- Parent POM over copy-pasted dependencies:
  one place controls Java version, Testcontainers versions, and plugin defaults.
- Multiple services early:
  JWT makes more sense when one service issues tokens and another validates them.
- PostgreSQL first:
  it avoids learning the wrong habits that come from H2-only demos.

## Spring MVC to Spring Boot comparison

- Old way:
  XML or hand-written Java config often wires datasources, view resolvers, and servlet infrastructure manually.
- Modern Boot way:
  the application declares intent through starters and properties, and Boot autoconfiguration does the repetitive setup.
- Old way:
  one monolith package often hides infrastructure coupling.
- Modern Boot way:
  each service owns a small, explicit boundary with its own config, schema, and runtime port.

## What to run

```powershell
docker compose up -d
./mvnw test
```

## What should happen

- PostgreSQL starts with `authdb`, `taskdb`, and `auditdb`
- Kafka starts and is visible in Kafka UI on port `8088`
- Maven discovers four modules

## What failure looks like

- Java 8 on your machine: Maven fails before compilation because the project targets Java 17
- Docker not running: containers never become healthy
- Wrong package names: Spring component scanning misses classes
