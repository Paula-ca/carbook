# Etapa de build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY settings.xml /root/.m2/settings.xml
COPY . .
RUN mvn clean package -DskipTests && ls -lh target

# Etapa final
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/carbook-1.0.0.jar app.jar
ENV PORT=8080
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
