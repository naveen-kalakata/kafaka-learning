# Phase 6: Kafka Reliability and Enterprise Patterns

## Reliability rules implemented

- Versioned topics:
  `user.registered.v1`, `task.created.v1`, `task.status-changed.v1`
- Aggregate IDs as Kafka keys:
  this preserves ordering for events that belong to the same user or task.
- Retry plus dead-letter topic:
  audit consumers retry transient failures and route poison messages to a `-dlt` topic.
- Idempotent audit persistence:
  duplicate deliveries do not create duplicate audit rows because `event_id` is unique.

## Why this design was chosen

- Kafka is at-least-once by design in many practical setups, so consumer code must tolerate duplicates.
- Ordering is per partition key, not across the whole topic.
- Dead-letter topics are a safety valve, not a substitute for fixing consumer bugs.

## Smoke flow

1. Register a user in `auth-service`
2. Log in and copy the JWT
3. Create a task in `task-service`
4. Change task status
5. Inspect audit rows through `audit-service`
6. Inspect topics in Kafka UI

## What to run

```powershell
docker compose up -d
./mvnw -pl auth-service spring-boot:run
./mvnw -pl task-service spring-boot:run
./mvnw -pl audit-service spring-boot:run
```

## What should happen

- user registration creates a `user.registered.v1` event
- task creation creates a `task.created.v1` event
- task status updates create a `task.status-changed.v1` event
- `audit-service` stores each event once

## What failure looks like

- wrong Kafka bootstrap server: producers or consumers never connect
- wrong JWT issuer/JWKS URL: secured requests fail with `401`
- duplicate consumer side effects: audit table gets repeated rows for the same `event_id`
