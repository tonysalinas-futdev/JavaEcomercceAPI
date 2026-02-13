# E-commerce

This is an application developed with **Spring** to provide features for an online store. The architectural pattern is a **modular monolith** because of its simplicity and ease of scaling, testing, and debugging. The project is in the final stages of development.

## Main features

- **Product and category management**: Full CRUD with validations and more.  
- **Authentication and authorization**: JWT; roles and fine-grained permissions.  
- **Shopping cart**: add, update, and remove items; total calculation.  
- **Payments**: integration with **Stripe** and use of events to handle completed payments.  
- **Messaging**: email sending for confirmations, notifications, and password recovery.  
- **Logging and traceability**: structured logs; `X-Correlation-ID` propagated in requests and logs.  
- **Internal events**: publish/consume events to decouple responsibilities.  
- **Design patterns**: use of patterns such as **Facade** to encapsulate complex subsystems.  
- **Versioning by URL**: versioned routes to ease API evolution.  
- **Testing**: unit and integration tests to validate critical flows.  
- **Idempotency**: support for idempotency keys on critical endpoints (payments, orders).

## Main modules

- **Auth** — login, signup, refresh tokens.  
- **Users** — user management, permissions, and roles.  
- **Categories** — CRUD for categories, searches, and filters.  
- **Product** — endpoints for products and categories; searches and filters.  
- **Cart** — cart management and its items per user.  
- **Payment** — Stripe integration; idempotency key handling; payment confirmation.  
- **Messaging** — templates and email sending (orders, confirmations, alerts).  
- **Logging** — centralized log configuration; propagation of `X-Correlation-ID`.  
- **Events** — domain events (`OrderCreated`, `PaymentSucceeded`, etc.).  
- **Tests** — unit and integration test suites; utilities for test data.

## Requirements

- **Java 21** (JDK 21)  
- **Gradle**  
- **PostgreSQL** (or compatible DB)  
- Set environment variables in a `.env` file, following the example in `.env.example`

## Run the application

1. Open a terminal  
2. `./gradlew build`  
3. `./gradlew spotlessApply` (if needed)  
4. `./gradlew bootRun`