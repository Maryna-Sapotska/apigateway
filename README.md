# Gateway Service

API Gateway built with Spring Cloud Gateway and WebFlux.

## Tech Stack

- Java 21
- Spring Boot 3 (WebFlux)
- Spring Cloud Gateway
- Spring Security
- Project Reactor
- JSON Web Token (JJWT)
- Docker
- Docker Compose
- Maven

---

## Responsibilities

The gateway is responsible for:

- Request routing
- JWT validation
- Authentication filtering
- Forwarding user information to downstream services via headers
- Centralized entry point for all microservices

## Features

- Route forwarding to microservices
- JWT authentication filter
- Public endpoints configuration
- User identity propagation using request headers
- Reactive non-blocking processing with WebFlux
- Dockerized deployment

---

## Security

All endpoints require a valid JWT token except:

- /api/auth/login
- /api/auth/register
- /actuator/**

After successful validation, the gateway forwards:

- X-User-Id
- X-User-Roles

to downstream services.

---

# Requirements

- Java 21
- Maven
- Docker Desktop

---

# Environment Variables

Required variables:

JWT_SECRET=your-secret-key

The same secret must be configured in:

- API Gateway
- Authentication Service

---

# Run Application Locally

## 1. Clone repository

```bash
git clone <repository-url>
cd gateway-service
```

## 2. Configure environment

Set JWT secret:

```bash
export JWT_SECRET=your-secret
```

## Run application

mvn spring-boot:run

Application will start on:

http://localhost:8083

# Run With Docker

## Build application

mvn clean package

## Build Docker image

docker build -t gateway-service .

## Start containers

docker compose up --build

--- 

# Routes

1. Authentication Service
   /api/auth/**

Forwarded to:

http://auth-service:8081

2. User Service
   /users/**

Forwarded to:

http://user-service:8080

---

# Author

Marina Sapotska