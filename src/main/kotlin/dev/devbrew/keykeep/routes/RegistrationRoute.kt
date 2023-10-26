package dev.devbrew.keykeep.routes

import dev.devbrew.keykeep.database.APIKey
import dev.devbrew.keykeep.database.Customer
import dev.devbrew.keykeep.database.CustomersTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun Routing.registerRegistrationRoute() {
    authenticate("auth") {
        post("/register") {
            val request = call.receive<RegistrationRequest>()
            val customer = transaction {
                Customer.find { CustomersTable.name eq request.customerName }.singleOrNull() ?: Customer.new {
                    name = request.customerName
                }
            }
            val apiKey = transaction {
                APIKey.new {
                    customerId = customer
                    this.apiKey = generateApiKey()
                    allowedIPs = request.allowedIps
                    information = request.information // assigning information
                }
            }
            call.respond(HttpStatusCode.Created, apiKey.apiKey)
        }
    }
}

@Serializable
data class RegistrationRequest(val customerName: String, val allowedIps: Int, val information: String)

fun generateApiKey(): String {
    // Implement your API key generation logic here
    return UUID.randomUUID().toString()
}