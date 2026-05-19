## 1. Validate Upstream Artifacts

- [x] 1.1 Confirm `specs/**/*.md` is reviewed and acceptance criteria are final.
- [x] 1.2 Confirm `design.md` is reviewed and stack/security decisions are finalized.
- [x] 1.3 Run `asyncapi-cli validate asyncapi.yaml` and resolve all errors.

## 2. Infrastructure Setup

- [x] 2.1 Add Nginx (Gateway) and Redis (Rate Limit Store) to `docker-compose.yml`.
- [x] 2.2 Create `nginx.conf` with base server block and upstream definitions.

## 3. Implementation - Routing & Security

- [x] 3.1 Implement path-based routing (`location` blocks) for `auth-service` and `chat-service`.
- [x] 3.2 Configure JWT validation using `auth_request` or a Lua script.
- [x] 3.3 Implement rate limiting using `limit_req_zone` (with Redis if using OpenResty).

## 4. Observability

- [ ] 4.1 Configure Nginx logging to send events to a sidecar or fluentd for Kafka ingestion.
- [ ] 4.2 Verify `gateway.events.v1` topic receives `RequestLogged` events.

## 5. Verification

- [ ] 5.1 Test successful routing to `auth-service`.
- [ ] 5.2 Test 401 Unauthorized for `chat-service` without token.
- [ ] 5.3 Test 429 Too Many Requests by exceeding the rate limit.
