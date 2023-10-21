# Build & run tests

## Build & run unit tests
>mvn clean install

## Run integration tests
>mvn failsafe:integration-test -PIT

## Run system tests
>mvn failsafe:integration-test -PST

---

# Run local (uses h2)
## With MVN
>mvn spring-boot:run -Dspring-boot.run.profiles=local

---

## Run as container
### Build container with docker
>docker build --tag=sample-boot-app:latest .

### Run container with Docker (+using profile)
>docker run -e "SPRING_PROFILES_ACTIVE=local" -p 8081:8081 sample-boot-app:latest 

---

# Run at dev (uses MySQL)
## Use docker-compose to build and run services, in detach mode
### Just to be sure that DB is ready for con before app starts,   
>docker-compose up --build --wait

---

# Run at higher environments (uses MySQL)
## **Adapt application.yml as per your environment
## Use docker-compose to build and run services, in detach mode
## Adapt docker-compose.yml as per your environment
>docker-compose up --build --wait
