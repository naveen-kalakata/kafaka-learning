# Phase 7: Migration Notes Tied to Features

## Security

- Old Spring Security:
  `WebSecurityConfigurerAdapter` concentrated many concerns into one inheritance-based class.
- Modern Spring Security:
  `SecurityFilterChain` uses explicit beans, which makes behavior clearer and easier to test.

## Configuration

- Old style:
  lots of manual property lookups or custom config classes.
- Modern Boot style:
  `@ConfigurationProperties` binds typed config objects directly from application settings.

## Servlet stack

- Old Spring MVC:
  much more manual servlet, dispatcher, and view configuration.
- Spring Boot 4:
  the servlet stack is still there, but Boot owns the standard plumbing so you can focus on application behavior.

## Jakarta migration

- Old imports:
  `javax.*`
- Current ecosystem:
  `jakarta.*`
- This matters because JPA, validation, and servlet APIs all moved packages.

## Integration style

- Old enterprise code often starts with synchronous service calls everywhere.
- This repo shows where Kafka is better:
  audit logging and integration events should not block the task write path.

## What to run

```powershell
./mvnw test
```

## What should happen

- each service starts with a small, explicit configuration surface
- security and eventing behavior are discoverable from code structure

## What failure looks like

- trying to port Java 8 patterns directly without revisiting configuration and security assumptions
