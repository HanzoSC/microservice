FROM maven:3.9.6-eclipse-temurin-21-alpine
WORKDIR /app
COPY pom.xml .
COPY src ./src
EXPOSE 8081
ENTRYPOINT ["mvn","spring-boot:run"]


