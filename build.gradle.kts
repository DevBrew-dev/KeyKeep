import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project
val prometheus_version: String = "0.11.0"

plugins {
    kotlin("jvm") version "1.9.10" // Update to the latest version if available
    id("io.ktor.plugin") version "2.3.5"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

group = "dev.devbrew.keykeep"
version = "0.0.1"

application {
    mainClass.set("dev.devbrew.keykeep.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version") // Added DAO dependency
    implementation("io.ktor:ktor-server-metrics-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.prometheus:simpleclient:$prometheus_version") // Added Prometheus dependencies
    implementation("io.prometheus:simpleclient_common:$prometheus_version")
    implementation("io.prometheus:simpleclient_hotspot:$prometheus_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.0")
    //redis
    implementation("io.github.crackthecodeabhi:kreds:0.9.0")



    shadow("io.ktor:ktor-server-core-jvm:$ktor_version")
    shadow("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    shadow("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    shadow("org.jetbrains.exposed:exposed-core:$exposed_version")
    shadow("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    shadow("org.jetbrains.exposed:exposed-dao:$exposed_version")
    shadow("io.ktor:ktor-server-metrics-jvm:$ktor_version")
    shadow("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    shadow("io.ktor:ktor-server-auth-jvm:$ktor_version")
    shadow("io.ktor:ktor-server-netty-jvm:$ktor_version")
    shadow("ch.qos.logback:logback-classic:$logback_version")
    shadow("io.prometheus:simpleclient:$prometheus_version")
    shadow("io.prometheus:simpleclient_common:$prometheus_version")
    shadow("io.prometheus:simpleclient_hotspot:$prometheus_version")
    shadow("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    shadow("io.ktor:ktor-server-tests-jvm:$ktor_version")
    shadow("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    shadow("io.ktor:ktor-server-cors:$ktor_version")
    shadow("org.postgresql:postgresql:42.6.0")
    shadow("io.github.cdimascio:dotenv-kotlin:6.4.0")
    shadow("io.github.crackthecodeabhi:kreds:0.9.0")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "20"
    }

    shadowJar {
        configurations = listOf(project.configurations.shadow.get())
        archiveFileName.set("backend.jar")
    }
}