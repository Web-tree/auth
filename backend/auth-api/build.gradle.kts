

group = "org.webtree.auth-api"
version = "0.0.1"

plugins {
    java
    id("org.springframework.boot") version "2.2.6.RELEASE"

    // Generate API documentation
    id("com.github.johnrengelman.processes") version "0.5.0"
    id("org.springdoc.openapi-gradle-plugin") version "1.1.0"
}

apply(plugin = "io.spring.dependency-management")

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("http://dl.bintray.com/merapar/maven")
    }

    maven {
        url = uri("https://repo.spring.io/libs-release")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-autoconfigure-processor")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("io.jsonwebtoken:jjwt:0.7.0")
    implementation("io.jsonwebtoken:jjwt-api:0.10.7")
    implementation("org.modelmapper:modelmapper:2.1.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.4")
    implementation("mysql:mysql-connector-java:8.0.13")
    implementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
    implementation("io.swagger:swagger-jersey2-jaxrs:1.6.1")
    implementation("org.springdoc:springdoc-openapi-ui:1.3.9")

    testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.0.5.RELEASE")
    testImplementation("org.springframework.security:spring-security-test:5.1.0.RELEASE")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("org.mockito:mockito-junit-jupiter:2.28.2")
    testImplementation("org.mock-server:mockserver-netty:3.10.8")
    testImplementation("org.testcontainers:junit-jupiter:1.11.3")
    testImplementation("org.testcontainers:mysql:1.11.3")
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

openApi {
    apiDocsUrl.set("http://localhost:9000/v3/api-docs")
    outputDir.set(file("$buildDir/docs"))
    outputFileName.set("swagger.json")
    waitTimeInSeconds.set(10)
}

tasks.test {
    useJUnitPlatform()
}
