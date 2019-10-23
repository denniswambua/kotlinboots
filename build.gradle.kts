import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.palantir.gradle.docker.DockerExtension
import org.gradle.kotlin.dsl.*
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "2.1.9.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	id("com.palantir.docker") version "0.22.1"
	kotlin("jvm") version "1.2.71"
	kotlin("plugin.spring") version "1.2.71"
	kotlin("plugin.jpa") version "1.2.71"
	kotlin("plugin.allopen") version "1.2.71"
	kotlin("kapt") version "1.2.71"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
	runtimeClasspath {
		extendsFrom(developmentOnly)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-mustache")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test"){
		exclude(module = "junit")
		exclude(module = "mockito-core")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("com.ninja-squad:springmockk:1.1.2")
	kapt("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

allOpen{
	annotations("javax.persistence.Entity")
	annotations("javax.persistence.Embeddable")
	annotations("javax.persistence.MappedSuperclass")
}


val bootJar = tasks["bootJar"] as BootJar

configure<DockerExtension> {
	name = "${project.group}/books:latest"
	files(bootJar.outputs)
	buildArgs(mapOf(
		"JAVA_OPTS" to "-Xms64m -Xmx128m",
		"JAVA_FILE" to "books-${project.version}.jar"
	))
	pull(true)
	dependsOn(bootJar, tasks["jar"])
}
