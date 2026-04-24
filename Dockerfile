# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests


# ---------- Package Stage ----------
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]