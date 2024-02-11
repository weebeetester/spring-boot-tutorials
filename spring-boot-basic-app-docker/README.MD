# Introduction
A very simplistic spring boot app, exposing REST APIs, and persisting data in database.
- locally, uses H2
- for other environments, run as docker container, with Mysql as db, all via docker-compose

# Build & run tests

## Build & run unit tests
>mvn clean install

## Run integration tests
>mvn failsafe:integration-test -PIT

## Run system tests
>mvn failsafe:integration-test -PST

---

# Run locally (uses h2)
## With MVN
>mvn spring-boot:run -Dspring-boot.run.profiles=local

---

## Run locally as container
### Build container with docker
>docker build -t spring-boot-basic-app-docker:latest .

### Run container with Docker (+using profile)
>docker run -e "SPRING_PROFILES_ACTIVE=local" -p 8081:8081 spring-boot-basic-app-docker:latest

---


# Run at higher environments (uses MySQL) : All via docker-compose
### **Adapt application.yml as per your environment
### Use docker-compose to build and run services, in detach mode
### Adapt docker-compose.yml as per your environment
>docker-compose up --build --wait
OR
>docker-compose up

## Test the app
```bash
>curl http://localhost:8081/api/students
```
