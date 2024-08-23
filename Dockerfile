FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/contractor-0.0.1-SNAPSHOT.jar /app/contractor.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/contractor.jar"]