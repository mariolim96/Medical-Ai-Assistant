## Context

This design covers the base routing, authentication, and rate limiting for the API Gateway as part of Phase 1 of the Medical AI Assistant architecture. It builds upon discovery in `event-storming.md` and `event-modeling.md`.

## Goals / Non-Goals

- **Goals:**
  - Standardized entry point for all microservices.
  - Centralized JWT validation.
  - Protection against traffic spikes via rate limiting.
- **Non-goals:**
  - Complex request/response body transformation.
  - Aggregation of multiple service calls into one (GraphQL-style).

## Messaging and Platform Decisions

### Broker / Runtime
- **Selected broker/runtime**: Nginx
- **Rationale**: Lightweight, extremely fast, and already familiar for many web architectures. Provides high performance for static content and reverse proxying.
- **Alternatives considered**: Kong (too much overhead for base routing), custom Go-based gateway.

### Subject/Topic Naming
- **Naming convention**: `gateway.events.<version>.<action>`
- **Versioning strategy**: Semantic versioning in the topic name.
- **Ownership conventions**: Owned by the Gateway team (Infra/Security).

### Payload and Schema Format
- **Message encoding format**: JSON
- **Schema format and evolution rules**: JSON Schema. Additive changes only for backward compatibility.
- **Compatibility expectations**: Must not break `analytics-service` consumers.

### Delivery Semantics and Reliability
- **At-most-once / at-least-once / exactly-once expectations**: At-least-once for logging events to Kafka.
- **Retry and dead-letter strategy**: Local retry for Kafka producer; ignore if Kafka is down to prevent blocking the request path.
- **Idempotency strategy**: Request IDs generated at the gateway and forwarded downstream.

## Security Decisions

- **Authentication approach**: JWT validation via Nginx Lua module (OpenResty) or `auth_request` directive. The gateway will verify the signature using the public key from `auth-service`.
- **Authorization model**: Role-based access control (RBAC) enforced by checking the `roles` claim in the decoded JWT.
- **Sensitive data handling**: Scrub sensitive headers; forward `X-User-ID` and `X-User-Role` to downstream services.
- **Transport security and secret management**: TLS 1.3 enforced. Secrets managed via environment variables in the Nginx container.

## Operations and Observability

- **Monitoring and alerting**: Prometheus plugin for request counts, latencies (P99), and error rates.
- **Trace/correlation strategy**: `Zipkin` or `Jaeger` plugin for distributed tracing via `X-Correlation-ID`.
- **Capacity/performance considerations**: Vertical scaling of Kong pods; Redis cluster for shared rate-limit counters.

## Risks / Trade-offs

- **[Risk] Single Point of Failure**
  - Mitigation: Deploy Kong in a High Availability (HA) configuration across multiple availability zones.
- **[Risk] Latency overhead**
  - Mitigation: Keep plugins lightweight; use Redis for rate limiting to minimize IO wait.

## Handoff to AsyncAPI

List concrete inputs required to author `asyncapi.yaml`:
- Channels: `gateway.events.v1.request_logged`, `gateway.events.v1.rate_limit_exceeded`
- Messages: Log message with user context, path, and latency.
- Bindings: Kafka
- Security schemes: TLS for Kafka connection.
