# Kafka Learning

Current flow in this repo:

- root app: JWT auth service and Kafka producer

End-to-end Kafka path:

1. Register a user in the root auth app
2. Auth app publishes `user-registered` event to Kafka
3. A separate `notification-service` project consumes that event

Run order:

```powershell
docker compose up -d
.\mvnw spring-boot:run
```

Ports:

- auth app: `8080`
- Kafka UI: `8088`
