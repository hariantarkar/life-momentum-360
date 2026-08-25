# ---- Build stage ----
# Compiles the app with Maven + JDK 21, produces the runnable jar.
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml first and download dependencies separately so Docker can cache
# this layer — dependencies only re-download when pom.xml actually changes,
# not on every source code edit.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the actual source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---- Runtime stage ----
# Slim JRE-only image — no Maven, no JDK, no source code, much smaller and
# a smaller attack surface than shipping the build stage itself.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy only the built jar from the build stage above
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]