FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# --no-cache ensures fresh build every time
RUN mvn clean package -DskipTests --no-transfer-progress

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 10000
ENV SERVER_PORT=10000
ENTRYPOINT ["java", "-jar", "app.jar"]