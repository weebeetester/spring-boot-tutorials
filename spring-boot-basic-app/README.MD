# Introduction
A very simplistic spring boot app, exposing REST APIs, and persisting data in database.
- locally uses H2
- for other, uses MySQL

# Testing

## Basics
**Definition by Michael Feathers in 2005

- A test is not a unit test if:
 -- It talks to the database
 -- It communicates across the network
 -- It touches the file system
 -- It can’t run at the same time as any of your other unit tests
 -- You have to do special things to your environment (such as editing config files) to run it
If your test does any of the above, it’s an integration test.

- Integration testing DOES NOT mean that you test the entire application. You could, for example, 
integration test your data access layer in isolation.

### Unit Testing 
- Do not depend on Spring application context (use new when needed without Spring)
- Just use JUnit5+Mockito, @ExtendWith(MockitoExtension),@Mock, @InjectMocks, when & assertions
### Narrow Integration Testing 
- Using only part of applicationContext (not full) via autoconfigure annotation, which will load only the beans required to test that 
  specific layer without loading other beans (Use @WebMvcTest, @DataJpaTest,...)
### Integration Testing 
- Using full applicationContext, so annotate with @SpringBootTest (will create applicationContext through SpringApplication)
- Up to us to decide if we use 
  - a mock web environment
  - a real web environment (real HTTP requests to app)
- Is not necessary to test the entire application
- May still use things like TestContainers, MockWebServer, etc to avoid connecting to external services for example.

## Testing Different Layers

### Testing Controller Layer:
- We [can] use @WebMvcTest(Annotation that can be used for a Spring MVC test that focuses only on Spring MVC components).
  - This Autoconfigure annotation only loads beans required for testing the web layer.
- For the dependencies (for example the service, which we dont want to test), use @MockBean.
- Do note that @WebMvcTest must be passed with the Controller class we want to test EXPLICITLY. Else it will scan all controllers 
  & we will be obliged to mock all dependencies used in all those controllers.
- Spring Boot also autoconfigures a MockMvc bean for us so that we can autowire that. Using MockMvc fakes HTTP requests for us, making 
  it possible to run the controller tests without starting an entire HTTP server.
- Arguably they might be called as narrow Integration tests (instead of unit tests) as they rely on Spring application context.

### Testing Data Layer:
- We [can] use @DataJpaTest (Annotation for a JPA test that focuses only on JPA components).
- This Autoconfigure annotation only loads beans required for testing the data layer.
- Arguably they might be called as narrow Integration tests (instead of unit tests) as they rely on Spring application context.
#### **Do not test the framework
- While testing data layer, it is not required to test the inherited methods (from JpaRepository for example) as this will be a waste
    (we do not want to test the framework itself). So only test what you explicitly added. Even you may want to skip the inferred methods/queries (findByName e.g.)
     even if they are explicitly added.
- Best candidate for testing are custom JPQL or native SQL Queries.
### Testing Other Layers (Service e.g.)
 - Do not depend on Spring application context (use new when needed without Spring)
 - Just use JUnit5+Mockito, @ExtendWith(MockitoExtension),@Mock, @InjectMocks, when & assertions


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

```bash
>docker-compose up

>docker container ls                                                   
CONTAINER ID   IMAGE          COMMAND                  CREATED          STATUS          PORTS                               NAMES
3e95641a7213   mysql:8.0.33   "docker-entrypoint.s…"   18 minutes ago   Up 18 minutes   0.0.0.0:3306->3306/tcp, 33060/tcp   spring-boot-basic-app-db-1

#Get an interactive shell to Mysql
>docker exec -it spring-boot-basic-app-db-1 mysql -uroot -proot
mysql: [Warning] Using a password on the command line interface can be insecure.
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 19
Server version: 8.0.33 MySQL Community Server - GPL

Copyright (c) 2000, 2023, Oracle and/or its affiliates.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> 
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| example_db         |
| information_schema |
| mysql              |
| performance_schema |
| root               |
| sys                |
+--------------------+
6 rows in set (0.01 sec)

mysql> use example_db
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
mysql> show tables;
+----------------------+
| Tables_in_example_db |
+----------------------+
| student              |
+----------------------+
1 row in set (0.00 sec)

mysql> 
mysql> select * from student;
+----+-----------------+---------+------+
| id | email           | major   | name |
+----+-----------------+---------+------+
|  1 | tim@hotmail.com | Physics | Tim  |
+----+-----------------+---------+------+
1 row in set (0.00 sec)


```

### **Adapt application.yml as per your environment
## then run the app
>mvn spring-boot:run 
