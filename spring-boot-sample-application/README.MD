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

# Run at higher environments (uses MySQL)
## First make sure DB is reachable
### To try out, you can run the DB in container:
>docker-compose up
### **Adapt application.yml as per your environment
## then run the app
>mvn spring-boot:run 
