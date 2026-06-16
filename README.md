# Auth Service

JWT-based authentication service built with Spring Boot, Spring Security, PostgreSQL, and Liquibase.
Supports registration, login, refresh token flow, and token validation.

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- JSON Web Token (JJWT)
- Liquibase
- Docker
- Docker Compose
- JUnit 5
- Maven

---

## Features
- User registration
- Login with JWT generation
- Access & Refresh tokens
- Token validation via JWT filter
- Role-based authentication (ROLE_USER, ROLE_ADMIN)
- Password encryption with BCrypt
- Database migrations via Liquibase
- PostgreSQL running in Docker


# Features

- Redis caching
- Liquibase migrations
- JPA auditing
- Specifications filtering
- Validation
- Exception handling
- Unit tests
- Integration tests with Testcontainers
- Docker support
- CI Pipeline with GitHub Actions

---

# Database

## Tables

### users_credentials

| Column   | Type                |
| -------- | ------------------- |
| id       | bigint (PK)         |
| login    | varchar(255) unique |
| email    | varchar(255)        |
| password | varchar(255)        |
| role     | varchar(50)         |
| active   | boolean             |

---

# Requirements

- Java 21
- Maven
- Docker Desktop

---

# Run Application Locally

## 1. Clone repository

```bash
git clone <repository-url>
cd authservice
```

## 2. Start PostgreSQL

docker compose up -d postgres

## Run application

mvn spring-boot:run

Application will start on:

http://localhost:8081

# Run With Docker

## Build application

mvn clean package

## Build Docker image

docker build -t authservice .

## Start containers

docker compose up --build

--- 

# Authentication Flow

1. Register
   POST /auth/register
   {
   "login": "user1",
   "password": "1234",
   "email": "user@test.com",
   "name": "John",
   "surname": "Doe"
   }

2. Login
   POST /auth/login

   Response:

   {
   "accessToken": "jwt_access_token",
   "refreshToken": "jwt_refresh_token"
   }

3. Refresh token
   POST /auth/refresh
   {
   "refreshToken": "jwt_refresh_token"
   }

4. Validate token
   GET /auth/validate
   Authorization: Bearer <accessToken>

   {
   "token": "jwt_refresh_token"
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