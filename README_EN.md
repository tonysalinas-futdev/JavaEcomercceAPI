# E-commerce

A Spring Boot application that provides the core functionalities of an online store. The project follows a Modular Monolith architecture due to its development and maintenance simplicity while preserving scalability, testability, and long-term maintainability.

## Main Features

* Product and category management: complete CRUD operations with validations and business rules.

* Authentication and authorization: JWT-based implementation with granular roles and permissions.

* Shopping cart: cart and cart item management, including automatic total calculation.

* Payments: Stripe integration and event-driven processing of completed payments.

* Messaging: email delivery for confirmations, password recovery, and notifications.

* Logging and traceability: structured technical, audit, and error logs; `X-Correlation-ID` propagation for end-to-end request tracing.

* Internal events: event publishing and consumption to decouple responsibilities across modules.

* API versioning: URL-based versioning to facilitate API evolution and maintenance.

* Testing: unit and integration tests covering critical business workflows.

* Caching: Cache-Aside pattern implementation using Redis to reduce latency and improve query performance.

* Idempotency: support for idempotency keys in critical operations such as payments and order creation.

* Environment profiles: dedicated configurations for development, testing, and production environments.

* Monitoring and observability: Spring Boot Actuator and Spring Boot Admin integration for application health monitoring, metrics, and operational management.

* Dockerized deployment: full Docker and Docker Compose support, including PostgreSQL and Redis.

## Main Modules

* Auth — authentication, registration, and refresh token management.

* Users — user, role, and permission management.

* Categories — category CRUD operations, searching, and filtering.

* Products — product management, searching, and filtering.

* Cart — shopping cart and cart item management per user.

* Payment — Stripe integration, payment confirmation, and idempotency handling.

* Messaging — email templates and delivery.

* Logging — centralized logging configuration and traceability propagation.

* Tests — documented unit and integration tests.

## Requirements

* Java 21

* Gradle

* PostgreSQL

* Redis

* Docker (optional)

* Docker Compose (optional)

* Configure the required environment variables following the `.env.example` file.

## Running the Application Locally

1. Open a terminal.

2. Build the project:

```bash
./gradlew build
```

3. Apply code formatting (if necessary):

```bash
./gradlew spotlessApply
```

4. Run the application:

```bash
./gradlew bootRun
```

## Docker Support

The application is fully containerized using Docker Compose.

Included services:

* Spring Boot Application
* PostgreSQL
* Redis

Containers use health checks to ensure the correct startup order and guarantee that the application starts only after its dependencies are available.

## Running with Docker

### 1. Configure Environment Variables

Create and configure the `.env` file using `.env.example` as a reference.

### 2. Build and Start All Services

```bash
docker compose up --build
```

### 3. Run Services in Detached Mode

```bash
docker compose up -d --build
```

### 4. Stop Services

```bash
docker compose down
```

### 5. Remove Services and Volumes

```bash
docker compose down -v
```

## API Documentation

The application includes interactive API documentation powered by Swagger/OpenAPI for exploring and testing available endpoints.

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

or

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

## Monitoring and Observability

The application exposes Spring Boot Actuator endpoints to monitor the health and behavior of system components.

Health endpoint:

```text
http://localhost:8080/actuator/health
```

Application information:

```text
http://localhost:8080/actuator/info
```

Metrics:

```text
http://localhost:8080/actuator/metrics
```

Additionally, the project integrates Spring Boot Admin, providing a centralized dashboard for monitoring application health, metrics, system information, and management endpoints.

## Technologies Used

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Bean Validation
* Spring Boot Actuator
* Spring Boot Admin
* PostgreSQL
* Redis
* Stripe Java SDK
* JWT
* Docker
* Docker Compose
* Gradle
* JUnit 5
* Mockito
* OpenAPI / Swagger