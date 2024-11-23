
plugins {
    id("org.springframework.boot") version "3.3.2"
    id("org.flywaydb.flyway") version "10.0.0"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven {
        url = uri("https://repository.jboss.org/nexus/content/groups/public/")
    }
}

dependencies {
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("com.auth0:jwks-rsa:0.22.1")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.2")
    implementation ("org.springframework.boot:spring-boot-starter-validation")
    implementation ("org.springframework.boot:spring-boot-starter-logging")
    implementation ("org.slf4j:slf4j-api:2.0.12")
    implementation("dev.samstevens.totp:totp:1.7.1")
    implementation("io.github.microutils:kotlin-logging:2.1.23")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")

//    implementation("io.lettuce.core:lettuce-core:6.1.5")
    implementation ("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    implementation ("io.jsonwebtoken:jjwt-impl:0.12.5")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation ("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation ("io.jsonwebtoken:jjwt-jackson:0.12.5")
    implementation ("org.springframework.boot:spring-boot-starter-mail")
    implementation ("org.flywaydb:flyway-database-postgresql:10.0.0")
    implementation ("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation ("com.cloudinary:cloudinary-http44:1.29.0")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation ("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}



