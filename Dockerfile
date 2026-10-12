FROM gradle:9.5.1-jdk21 AS builder

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jre-noble

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar