# Build
FROM openjdk:11-jdk-buster as builder

RUN mkdir /auth-api
WORKDIR /auth-api
COPY . .
RUN ./gradlew bootJar

# Main
FROM openjdk:11-jre-buster
COPY --from=builder /auth-api/build/libs/auth-*.jar /app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar", "/app.jar"]
