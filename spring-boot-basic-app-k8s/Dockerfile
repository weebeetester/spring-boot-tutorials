FROM eclipse-temurin:17-jre as builder
# Exploding the uber jar to be able to optimize docker layers
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Building the image
FROM eclipse-temurin:17-jre
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
#ENTRYPOINT ["java", "-Dspring.profiles.active=local", "org.springframework.boot.loader.launch.JarLauncher"]