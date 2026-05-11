plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.xapps"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2025.0.0"

dependencies {
	// Core Spring Boot
	implementation("org.springframework.boot:spring-boot-starter-web") // keep or swap to webflux if you want pure reactive endpoints
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Reactive DB (R2DBC) - explicit driver coordinates/versions so Gradle resolves them
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.postgresql:r2dbc-postgresql:1.0.7.RELEASE")   // R2DBC Postgres driver (org.postgresql group)

	// JDBC only for Flyway migrations (JDBC Postgres driver)
	implementation("org.postgresql:postgresql")
	implementation("org.flywaydb:flyway-core")

	// RabbitMQ Streams
	implementation("org.springframework.amqp:spring-rabbit-stream")

	// Jackson + Kotlin + Reactor
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	// Extra libs
	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
	implementation("io.github.resilience4j:resilience4j-spring-boot2:1.7.1")
	implementation("io.github.resilience4j:resilience4j-timelimiter:1.7.1")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")


	// Test dependencies
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("org.springframework.security:spring-security-test")

	// In-memory R2DBC DB for tests (explicit version)
	testImplementation("io.r2dbc:r2dbc-h2:1.0.0.RELEASE")
	testImplementation("com.h2database:h2")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
