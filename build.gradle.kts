import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project
val prometheus_version: String = "0.16.0"

fun DependencyHandler.scope(group: String, name: String, version: String?) {
    add("implementation", "$group:$name:$version")
    add("shadow", "$group:$name:$version")
}

plugins {
    kotlin("jvm") version "1.9.20"
    id("io.ktor.plugin") version "2.3.5"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
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
    scope("io.ktor", "ktor-server-core-jvm", ktor_version)
    scope("io.ktor", "ktor-serialization-kotlinx-json-jvm", ktor_version)
    scope("io.ktor", "ktor-server-content-negotiation-jvm", ktor_version)
    scope("org.jetbrains.exposed", "exposed-core", exposed_version)
    scope("org.jetbrains.exposed", "exposed-jdbc", exposed_version)
    scope("org.jetbrains.exposed", "exposed-dao", exposed_version)
    scope("io.ktor", "ktor-server-metrics-jvm", ktor_version)
    scope("io.ktor", "ktor-server-call-logging-jvm", ktor_version)
    scope("io.ktor", "ktor-server-auth-jvm", ktor_version)
    scope("io.ktor", "ktor-server-netty-jvm", ktor_version)
    scope("ch.qos.logback", "logback-classic", logback_version)
    scope("io.prometheus", "simpleclient", prometheus_version)
    scope("io.prometheus", "simpleclient_common", prometheus_version)
    scope("io.prometheus", "simpleclient_hotspot", prometheus_version)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    scope("io.ktor", "ktor-server-tests-jvm", ktor_version)
    scope("org.jetbrains.kotlin", "kotlin-test-junit", kotlin_version)
    scope("io.ktor", "ktor-server-cors", ktor_version)
    scope("org.postgresql", "postgresql", "42.6.0")
    scope("io.github.cdimascio", "dotenv-kotlin", "6.4.0")
    scope("io.github.crackthecodeabhi", "kreds", "0.9.0")
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