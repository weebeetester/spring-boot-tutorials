# Introduction
A very simplistic spring boot app, exposing REST APIs, and persisting data in database.
- locally, uses H2
- for other environments, uses MySQL (via docker-compose)

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
## First make sure DB is reachable (see below)
### To try out, you can run the DB in container:
>docker-compose up

```bash
>docker-compose up
```

### Adapt application.yml as per your environment
## then run the app
>mvn spring-boot:run

```bash
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


