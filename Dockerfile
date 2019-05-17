# Build
FROM webtree/build-images:maven-jdk-8 as builder

RUN mkdir /build
WORKDIR /build
COPY . .
WORKDIR /build/auth-core
RUN mvn package -DskipTests

# Main
FROM openjdk:11-slim
COPY --from=builder /build/auth-core/target/*-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar", "/app.jar"]