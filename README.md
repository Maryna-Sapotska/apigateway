# Gateway Service

A gateway service responsible for user registration and coordination between auth-service and user-service.

## Tech Stack

- Java 21
- Spring Boot 3 (WebFlux)
- Spring Security
- Spring WebClient
- Project Reactor
- JSON Web Token (JJWT)
- Docker
- Docker Compose
- Maven

---

## Features
- Distributed user registration across services
- Integration with auth-service and user-service
- JWT-based authentication flow
- Reactive non-blocking pipeline (WebFlux + Mono)
- Centralized error handling via onStatus
- Rollback mechanism for consistency
- Email conflict detection (409 CONFLICT handling)
- JWT parsing and userId extraction
- Admin token-based secure service-to-service communication

# Features

- Reactive orchestration of multiple services
- Cross-service registration consistency
- Compensation logic (rollback on failure)
- Structured error handling per service
- Duplicate email detection (HTTP + message-based)
- Service-to-service authentication
- Clean separation of auth and user creation logic
- Centralized mapping of external errors to domain exceptions

---

# Requirements

- Java 21
- Maven
- Docker Desktop
- Running auth-service
- Running user-service

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

# API

1. Register User
   POST api//auth/register
   {
   "login": "user3",
   "password": "123456",
   "email": "user3@test.com",
   "name": "John",
   "surname": "Doe",
   "birthDate": "2000-01-01"
   }

2. Login
   POST api/auth/login

   Response:

   {
   "accessToken": "jwt_access_token",
   "refreshToken": "jwt_refresh_token"
   }

---

# CI Pipeline

GitHub Actions pipeline includes:

- Build
- SonarQube analysis
- Docker image build

Pipeline configuration located in:

.github/workflows/ci.yml

---

# Author

Marina Sapotska