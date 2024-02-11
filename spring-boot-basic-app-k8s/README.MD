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

# For other environments, uses K8s (uses MySQL 8)

## Prepare docker image
### Build docker image
>docker build -t weebeetester/spring-boot-basic-app:latest .

### Push docker image to docker hub (might require 'docker login')
>docker image push weebeetester/spring-boot-basic-app:latest

## Start local (minikube) cluster
>minikube start --memory=4096 --driver=hyperkit

## Deploy objects
### see devops/kubernetes folder
>kubectl apply -f mysql-pv.yml

>kubectl apply -f mysql-deployment.yml

>kubectl apply -f k8s-configmap.yml

>kubectl apply -f k8s-deployment.yml

>kubectl apply -f k8s-service.yml

App will be reachable at http://${MINIKUBE_IP}:${NODEPORT}

---

# Troubleshooting

## NodePort service not working with minikube on mac
https://stackoverflow.com/questions/55591468/unable-to-access-a-nodeport-service-on-minikube

### Remove old minikube cluster
>minikube delete
### Start minikube with hyperkit driver:
>minikube start --memory=4096 --driver=hyperkit

### Run minikube ip. You'll see an output like 192.168.99.100.

### Then, create again the Pods and the service and it should work properly.



## List minikube services
>minikube service list

## List minikube images
>minikube image list

## Remove image from minikube
>minikube image rm docker.io/weebeetester/sample-spring-boot-app:v1

## Make image available to minikube
https://stackoverflow.com/questions/6865538/solving-a-communications-link-failure-with-jdbc-and-mysql
https://stackoverflow.com/questions/48262498/how-to-fix-this-java-mysql-exception-communications-link-failure
https://stackoverflow.com/questions/42564058/how-to-use-local-docker-images-with-minikube
### Start minikube
>minikube start
### Set docker env
>eval $(minikube docker-env)             # unix shells

>minikube docker-env | Invoke-Expression # PowerShell
### check and remove old image to be sure
>minikube image list

>minikube image rm docker.io/weebeetester/sample-spring-boot-app:latest

### Build image
>docker build -t weebeetester/sample-spring-boot-app:latest .
### Run in minikube
>kubectl run hello-foo --image=foo:0.0.1 --image-pull-policy=Never
### Check that it's running
>kubectl get pods


## Check if Mysql setup is ok
>kubectl exec -it mysql-57cc665b4c-jwfkq -- bash
>>mysql -uroot -proot
>>>show databases;

### Or one-liner (without terminal)
>kubectl exec -i mysql-57cc665b4c-jwfkq -- mysql -uroot -proot <<<'select convert_tz(now(), "UTC", "CET")'

## Test connection from other/app pods in the cluster
>kubectl exec -it spring-boot-sample-application-deployment-599b74577d-pmcfw -- bash
>>root@spring-boot-sample-application-deployment-599b74577d-pmcfw:/application# curl -v mysql:3306
*   Trying 10.98.111.118:3306...
* Connected to mysql (10.98.111.118) port 3306 (#0)
> GET / HTTP/1.1
> Host: mysql:3306
> User-Agent: curl/7.81.0
> Accept: */*
>
* Received HTTP/0.9 when not allowed
* Closing connection 0
  curl: (1) Received HTTP/0.9 when not allowed
