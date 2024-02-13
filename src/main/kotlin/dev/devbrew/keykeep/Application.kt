package dev.devbrew.keykeep

import dev.devbrew.keykeep.plugins.configureMonitoring
import dev.devbrew.keykeep.plugins.configureRouting
import dev.devbrew.keykeep.plugins.configureSerialization
import dev.devbrew.keykeep.routes.apiKeyValidationRoute
import dev.devbrew.keykeep.routes.registerRegistrationRoute
import dev.devbrew.keykeep.services.DatabaseService
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

fun main() {
    DatabaseService.init()
    embeddedServer(
        Netty,
        port = 8420,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    val dotenv = dotenv { systemProperties = true }
    val adminUsername = dotenv["ADMIN_USERNAME"]
    val adminPassword = dotenv["ADMIN_PASSWORD"]

    loadModule("Monitoring Module") {
        configureMonitoring()
    }
    loadModule("Serialization Module") {
        configureSerialization()
    }
    loadModule("Authentication Module") {
        setupAuthentication(adminUsername, adminPassword)
    }
    loadModule("Routing Module") {
        setupRoutes()
    }
}

fun Application.loadModule(moduleName: String, installModule: () -> Unit) {
    installModule.invoke()
    println("$moduleName loaded successfully.")
}

fun Application.setupAuthentication(adminUsername: String, adminPassword: String) {
    install(Authentication) {
        basic("auth") {
            realm = "ktor application"
            validate { credentials ->
                validateCredentials(credentials, adminUsername, adminPassword)
            }
        }
    }
}

fun validateCredentials(
    credentials: UserPasswordCredential,
    adminUsername: String,
    adminPassword: String
): UserIdPrincipal? {
    return if (
        credentials.name == adminUsername
        && credentials.password == adminPassword
    ) {
        UserIdPrincipal(credentials.name)
    } else null
}

fun Application.setupRoutes() {
    configureRouting()
    routing {
        registerRegistrationRoute()
        apiKeyValidationRoute()
    }
}