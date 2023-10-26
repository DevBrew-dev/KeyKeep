package dev.devbrew.keykeep.routes

import dev.devbrew.keykeep.database.APIKey
import dev.devbrew.keykeep.database.APIKeysTable
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.apiKeyValidationRoute() {
    get("/validate/{apiKey}") {
        val apiKeyParam = call.parameters["apiKey"]
        if (apiKeyParam != null) {
            val isValid = transaction {
                APIKey.find { APIKeysTable.apiKey eq apiKeyParam }.singleOrNull() != null
            }
            call.respond(mapOf("isValid" to isValid))
        } else {
            call.respond(mapOf("error" to "API Key parameter is missing"))
        }
    }
}
