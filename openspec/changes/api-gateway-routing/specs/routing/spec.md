## ADDED User Stories

### User Story: Path-based routing
As a developer, I want the gateway to forward requests to the correct microservice based on the URL path, so that internal services are abstracted from the client.

#### Acceptance Criteria
- **Given** the gateway is running with a defined routing table
- **When** an HTTP GET request is received for `/api/v1/auth/me`
- **Then** the request should be forwarded to the `auth-service`
- **And** the client should receive the response from the `auth-service`

### User Story: Auth enforcement
As a security officer, I want all protected routes to require a valid JWT, so that sensitive medical data is protected.

#### Acceptance Criteria
- **Given** a request to a protected endpoint like `/api/v1/chat/message`
- **When** the `Authorization` header is missing or contains an invalid token
- **Then** the gateway should reject the request with a `401 Unauthorized` status
- **And** the request should NOT be forwarded to the downstream service

### User Story: Rate limiting
As a system admin, I want to limit the frequency of requests from a single IP, so that the system remains stable under heavy load.

#### Acceptance Criteria
- **Given** a client IP has exceeded the threshold of 60 requests per minute
- **When** a new request is received from that same IP
- **Then** the gateway should return a `429 Too Many Requests` status
- **And** a `Retry-After` header should be included in the response
