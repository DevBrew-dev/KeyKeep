package dev.devbrew.keykeep

import dev.devbrew.keykeep.plugins.*
import dev.devbrew.keykeep.routes.registerRegistrationRoute
import dev.devbrew.keykeep.services.DatabaseService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun main() {
    DatabaseService.init()
    embeddedServer(
        Netty,
        port = 8420,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureMonitoring()
    configureSerialization()

    install(Authentication) {
        basic("auth") {
            realm = "ktor application"
            validate { credentials ->
                if (credentials.name == "cancelcloud" && credentials.password == "cancelcloud") {
                    UserIdPrincipal(credentials.name)
                } else null
            }
        }
    }

    configureRouting()
    routing {
        registerRegistrationRoute()
    }
}


@Serializable
data class RegistrationRequest(val customerName: String, val allowedIps: Int)

