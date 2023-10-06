# Build & run tests

## Build & run unit tests
mvn clean install

## Run integration tests
mvn failsafe:integration-test -PIT

## Run system tests
mvn failsafe:integration-test -PST

---

# Run local (uses h2)
## With MVN
mvn spring-boot:run -Dspring-boot.run.profiles=local

---

# Run at dev (uses MySQL)
## First make sure DB is reachable (start the DB as container)
docker-compose up
## then run the app
mvn spring-boot:run -Dspring-boot.run.profiles=dev

---

# Run at higher environments (uses MySQL)
## First make sure DB is reachable 
### **Adapt application.yml as per your environment
## then run the app
mvn spring-boot:run 

---

# Troubleshooting

## List minikube services
minikube service list

## List minikube images
minikube image list

## Remove image from minikube
minikube image rm docker.io/weebeetester/sample-spring-boot-app:v1

## Make image available to minikube
https://stackoverflow.com/questions/6865538/solving-a-communications-link-failure-with-jdbc-and-mysql
https://stackoverflow.com/questions/48262498/how-to-fix-this-java-mysql-exception-communications-link-failure
https://stackoverflow.com/questions/42564058/how-to-use-local-docker-images-with-minikube
### Start minikube
minikube start
### Set docker env
eval $(minikube docker-env)             # unix shells
minikube docker-env | Invoke-Expression # PowerShell
### check and remove old image to be sure 
minikube image list
minikube image rm docker.io/weebeetester/sample-spring-boot-app:latest

### Build image
docker build -t weebeetester/sample-spring-boot-app:latest .
### Run in minikube
kubectl run hello-foo --image=foo:0.0.1 --image-pull-policy=Never
### Check that it's running
kubectl get pods





## Check if Mysql setup is ok
kubectl exec -it mysql-57cc665b4c-jwfkq -- bash
>mysql -uroot -proot
show databases;

### Or one-liner (without terminal)
kubectl exec -i mysql-57cc665b4c-jwfkq -- mysql -uroot -proot <<<'select convert_tz(now(), "UTC", "CET")'

## Test connection from other/app pods in the cluster
kubectl exec -it spring-boot-sample-application-deployment-599b74577d-pmcfw -- bash
root@spring-boot-sample-application-deployment-599b74577d-pmcfw:/application# curl -v mysql:3306
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


## NodePort service not working with minikube on mac
https://stackoverflow.com/questions/55591468/unable-to-access-a-nodeport-service-on-minikube

### Remove old minikube
minikube delete
### Start minikube with virtualbox driver:
minikube start --memory=4096 --driver=hyperkit

### Run minikube ip. You'll see an output like 192.168.99.100.

### Then, create again the Pods and the service and it should work properly.