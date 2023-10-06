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

# Run at dev (uses MySQL)
## First make sure DB is reachable (start the DB as container)
>docker-compose up
## then run the app
>mvn spring-boot:run -Dspring-boot.run.profiles=dev

---

# Run at higher environments (uses MySQL)
## First make sure DB is reachable 
### **Adapt application.yml as per your environment
## then run the app
>mvn spring-boot:run 
