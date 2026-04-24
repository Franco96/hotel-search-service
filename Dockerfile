# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
# Descarga dependencias solo si el pom.xml cambia
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime (Más liviano y seguro)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8081
# Configuración recomendada para Java en contenedores
ENTRYPOINT ["java", "-XX:+UseParallelGC", "-jar", "app.jar"]
