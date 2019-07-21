import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.sonarqube.gradle.SonarQubeTask

plugins {
	kotlin("jvm") version "1.3.21"

	// By default Kotlin makes classes final which conflicts with Spring which needs classes to be open.
	// The kotlin-spring is a wrapper on top of the all-open plugin.
	// https://kotlinlang.org/docs/reference/compiler-plugins.html#spring-support
	id("org.jetbrains.kotlin.plugin.spring")        version "1.3.21"

	// Generates an additional zero-arg constructor needed by JPA to instantiate an entity.
	// It's a wrapper on top of the no-arg plugin:
	// https://kotlinlang.org/docs/reference/compiler-plugins.html#jpa-support
	id("org.jetbrains.kotlin.plugin.jpa")           version "1.3.21"

	// The Spring Boot Gradle Plugin provides Spring Boot support in Gradle, allowing you to package
	// executable jar or war archives, run Spring Boot applications, and use the dependency management
	// provided by spring-boot-dependencies.
	id("org.springframework.boot")                  version "2.1.3.RELEASE" // Note: Make sure this is in sync with Cornerstone Spring Boot version

	// Code Coverage plugin
	jacoco

	// Kotlin linter
	id("org.jlleitschuh.gradle.ktlint")             version "7.1.0"

	// dependencyUpdates - a task to determine which dependencies have updates
	id("com.github.ben-manes.versions")             version "0.21.0"

	// SonarQube is a code review tool to detect bugs, vulnerabilities and code smells
	id("org.sonarqube")                             version "2.7"

	id("com.google.cloud.tools.jib") version "1.4.0"
}

configure<JavaPluginConvention> {
	group = "com.example.workshop"
	version = "0.0.1-SNAPSHOT"
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

// Null safety for Kotlin projects through the Spring Initializr:
// https://github.com/spring-io/initializr/issues/591
tasks.withType<KotlinCompile<KotlinJvmOptions>> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

repositories {
	mavenCentral()
	jcenter()
}

jib {
	container {
		ports = listOf("8102")
		mainClass = "com.example.workshop.WorkshopApplicationKt"

		// good defauls intended for Java 8 (>= 8u191) containers
		jvmFlags = listOf(
			"-server",
			"-Djava.awt.headless=true",
			"-XX:InitialRAMFraction=2",
			"-XX:MinRAMFraction=2",
			"-XX:MaxRAMFraction=2",
			"-XX:+UseG1GC",
			"-XX:MaxGCPauseMillis=100",
			"-XX:+UseStringDeduplication"
		)
	}
}

// Jacoco Plugin
jacoco {
	toolVersion = "0.8.4"
	reportsDir = file("$buildDir/reports/jacoco")
}

// Jacoco Coverage Report
val jacocoTestReport by tasks.getting(JacocoReport::class) {
	reports {
		html.isEnabled = true // human readable
		xml.isEnabled = true  // required by coveralls
	}
	doLast {
		println("Jacoco tests coverage report: " + project.projectDir.toString() + "/build/reports/jacoco/test/html/index.html")
	}
}

// Jacoco Enforce Code Coverage
val jacocoTestCoverageVerification by tasks.getting(JacocoCoverageVerification::class) {
	violationRules {
		// Every class should be tested
		rule {
			enabled = true
			element = "CLASS"
			includes = listOf("com.example.workshop.*")

			// Coverage for classes. Strive for 75%.
			limit {
				counter = "CLASS"
				value = "COVEREDRATIO"
				minimum = BigDecimal.valueOf(0.75)

				excludes = listOf(
					"com.example.workshop.WorkshopApplication",
					"com.example.workshop.WorkshopApplicationKt"
				)
			}
		}

		// Coverage on lines of code. Strive for 75%.
		rule {
			enabled = true
			element = "CLASS"
			includes = listOf("com.example.workshop.*")

			limit {
				counter = "LINE"
				value = "COVEREDRATIO"
				minimum = BigDecimal.valueOf(0.75)

				excludes = listOf(
					"com.example.workshop.WorkshopApplication",
					"com.example.workshop.WorkshopApplicationKt"
				)
			}
		}
	}
}

// Test settings
val test by tasks.getting(Test::class) {
	useJUnitPlatform()  // enable Gradle to run JUnit 5 tests
	// log skipped and failed tests
	testLogging {
		events = setOf(TestLogEvent.SKIPPED, TestLogEvent.FAILED)
		showStandardStreams = true
	}
}

sourceSets.create("testUtil") {
	withConvention(KotlinSourceSet::class) {
		kotlin.srcDirs("src/testUtil/kotlin")
		resources.srcDirs("src/testUtil/resources")
		compileClasspath += sourceSets["main"].output
		runtimeClasspath += sourceSets["main"].output
	}
}

configurations.getByName("testUtilCompile").extendsFrom(configurations["compile"])
configurations.getByName("testUtilRuntime").extendsFrom(configurations["runtime"])

val testUtilCompile = configurations.getByName("testUtilCompile")
val testUtilRuntime = configurations.getByName("testUtilRuntime")

sourceSets.getByName("test") {
	withConvention(KotlinSourceSet::class) {
		kotlin.srcDirs("src/test/kotlin")
		resources.srcDirs("src/test/resources")
		compileClasspath += sourceSets["main"].output + sourceSets["testUtil"].output
		runtimeClasspath += sourceSets["main"].output+ sourceSets["testUtil"].output
	}
}

configurations.getByName("testCompile").extendsFrom(configurations["testUtilCompile"])
configurations.getByName("testRuntime").extendsFrom(configurations["testUtilRuntime"])

sourceSets.create("componentTest") {
	withConvention(KotlinSourceSet::class) {
		kotlin.srcDirs("src/componentTest/kotlin")
		resources.srcDirs("src/componentTest/resources")
		compileClasspath += sourceSets["main"].output + sourceSets["testUtil"].output
		runtimeClasspath += sourceSets["main"].output + sourceSets["testUtil"].output
	}
}

configurations.getByName("componentTestCompile").extendsFrom(configurations["compile"], configurations["testUtilCompile"])
configurations.getByName("componentTestRuntime").extendsFrom(configurations["runtime"], configurations["testUtilRuntime"])

val componentTestCompile = configurations.getByName("componentTestCompile")
val componentTestRuntime = configurations.getByName("componentTestRuntime")

tasks.register<Test>("componentTest") {
	description = "Runs Cucumber component tests."
	group = "verification"
	testClassesDirs = sourceSets["componentTest"].output.classesDirs
	classpath = sourceSets["componentTest"].runtimeClasspath
	outputs.upToDateWhen { false }

	testLogging {
		events = setOf(TestLogEvent.SKIPPED, TestLogEvent.FAILED)
		showStandardStreams = true
	}
	systemProperty("cucumber.options", System.getProperty("scenario"))
}

sourceSets.create("contractTest") {
	withConvention(KotlinSourceSet::class) {
		kotlin.srcDirs("src/contractTest/kotlin")
		resources.srcDirs("src/contractTest/resources")
		compileClasspath += sourceSets["main"].output + sourceSets["testUtil"].output
		runtimeClasspath += sourceSets["main"].output + sourceSets["testUtil"].output
	}
}

configurations.getByName("contractTestCompile").extendsFrom(configurations["compile"], configurations["testUtilCompile"])
configurations.getByName("contractTestRuntime").extendsFrom(configurations["runtime"], configurations["testUtilRuntime"])

val contractTestCompile = configurations.getByName("contractTestCompile")
val contractTestRuntime = configurations.getByName("contractTestCompile")

tasks.register<Test>("contractTest") {
	description = "Runs Cucumber contract tests."
	group = "verification"
	testClassesDirs = sourceSets["contractTest"].output.classesDirs
	classpath = sourceSets["contractTest"].runtimeClasspath
	outputs.upToDateWhen { false }

	testLogging {
		events = setOf(TestLogEvent.SKIPPED, TestLogEvent.FAILED)
		showStandardStreams = true
	}
	systemProperty("cucumber.options", System.getProperty("scenario"))
}

val ktlintCheck: DefaultTask by tasks
test.dependsOn(ktlintCheck)
test.dependsOn(tasks["componentTest"])
test.dependsOn(tasks["contractTest"])
test.finalizedBy(jacocoTestCoverageVerification, jacocoTestReport)

// execute linters on check
val check: DefaultTask by tasks
check.dependsOn(ktlintCheck)

// always run tests before code coverage is collected
jacocoTestReport.dependsOn(test)
jacocoTestCoverageVerification.dependsOn(test)

// make sure SonarQube depends on the Jacoco test report
val sonarqube: SonarQubeTask by tasks
sonarqube.dependsOn(jacocoTestReport)

val springBootVersion = "2.1.4.RELEASE"
val cucumberVersion = "4.3.0"
val wiremockVersion = "2.1.1.RELEASE"
val swagger2Version = "2.9.2"
val junit4Version = "4.12"
val logbookVersion = "2.0.0-RC.0"

dependencies {

	// Kotlin libraries
	implementation(platform(kotlin("bom", version = "1.3.21")))
	implementation(kotlin("stdlib-jdk8"))
	implementation(kotlin("reflect"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
	compile(kotlin("script-runtime"))

	implementation("ch.qos.logback:logback-classic:1.2.3")
	implementation("ch.qos.logback:logback-core:1.2.3")
	implementation("org.slf4j:slf4j-api:1.7.26")
	implementation("org.codehaus.janino:janino:2.6.1")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.9.9.1")

	implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")

	implementation("se.transmode.gradle:gradle-docker:1.2")

	//MySQL
	implementation("mysql:mysql-connector-java:8.0.16")

	// Swagger OpenApi spec
	implementation("io.springfox:springfox-swagger2:$swagger2Version")
	implementation("io.springfox:springfox-swagger-ui:$swagger2Version")

	// Spring Boot automatic re-run in Intelij Idea
	implementation("org.springframework.boot:spring-boot-devtools:$springBootVersion")

	// Spring Boot logging request and response
//	implementation("org.zalando:logbook-spring-boot-starter:$logbookVersion")

	// JMXMP Remote: https://stackoverflow.com/a/41230062/9698467
	implementation("org.jvnet.opendmk:jmxremote_optional:1.0_01-ea")

	// Apache Commons
	implementation("org.apache.commons:commons-lang3:3.8.1")

	// Slugify normalizes strings with special characters into human-readable, lower-case,
	// ASCII strings with words hyphenated with '-'. It's usually used for URLs.
	implementation("com.github.slugify:slugify:2.3")

	// Create fake names for a variety of things: https://github.com/DiUS/java-faker
	implementation("com.github.javafaker:javafaker:0.17.2")

	//Spring Boot Starter Test
	testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion") {
		exclude(module = "junit")
	}
	// JUnit 5
	testImplementation("org.junit.platform:junit-platform-console:1.4.0")
	testImplementation(enforcedPlatform("org.junit:junit-bom:5.3.2"))   		  // use JUnit 5
	testImplementation("org.junit.jupiter:junit-jupiter-api")           // JUnit 5 public API for writing tests and extensions
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")           // JUnit 5 engine to run tests
	testImplementation("org.junit.jupiter:junit-jupiter-params")        // JUnit 5 parameterized tests

	//Cucumber support libraries for integration tests with mocks
	componentTestCompile(platform(kotlin("bom", version = "1.3.21")))
	componentTestCompile(kotlin("stdlib-jdk8"))
	componentTestCompile(kotlin("reflect"))

	componentTestCompile("io.springfox:springfox-swagger2:$swagger2Version")
//	componentTestCompile("org.zalando:logbook-spring-boot-starter:$logbookVersion")

	componentTestCompile("io.cucumber:cucumber-java:$cucumberVersion")
	componentTestCompile("io.cucumber:cucumber-junit:$cucumberVersion")
	componentTestCompile("io.cucumber:cucumber-spring:$cucumberVersion")
	componentTestCompile("junit:junit:$junit4Version")
	componentTestCompile("org.springframework.cloud:spring-cloud-contract-wiremock:$wiremockVersion")

	//Cucumber support libraries for contract tests
	contractTestCompile(platform(kotlin("bom", version = "1.3.21")))
	contractTestCompile(kotlin("stdlib-jdk8"))

	contractTestCompile("io.springfox:springfox-swagger2:$swagger2Version")
	contractTestCompile("org.springframework.boot:spring-boot-starter-amqp:$springBootVersion")
//	contractTestCompile("org.zalando:logbook-spring-boot-starter:$logbookVersion")

	contractTestCompile("io.cucumber:cucumber-java:$cucumberVersion")
	contractTestCompile("io.cucumber:cucumber-junit:$cucumberVersion")
	contractTestCompile("io.cucumber:cucumber-spring:$cucumberVersion")
	contractTestCompile("junit:junit:$junit4Version")

	//testUtil dependencies
	testUtilCompile(platform(kotlin("bom", version = "1.3.21")))
	testUtilCompile(kotlin("stdlib-jdk8"))
	testUtilCompile("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")

	testUtilCompile("io.springfox:springfox-swagger2:$swagger2Version")
	testUtilCompile("org.springframework.boot:spring-boot-starter-amqp:$springBootVersion")
//	testUtilCompile("org.zalando:logbook-spring-boot-starter:$logbookVersion")

	testUtilCompile("io.cucumber:cucumber-java:$cucumberVersion")
	testUtilCompile("io.cucumber:cucumber-junit:$cucumberVersion")
	testUtilCompile("io.cucumber:cucumber-spring:$cucumberVersion")
	testUtilCompile("junit:junit:$junit4Version")
	testUtilCompile("org.springframework.cloud:spring-cloud-contract-wiremock:$wiremockVersion")
}
