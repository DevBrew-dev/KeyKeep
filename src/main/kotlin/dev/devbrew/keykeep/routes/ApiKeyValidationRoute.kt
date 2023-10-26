package dev.devbrew.keykeep.routes

import dev.devbrew.keykeep.database.APIKey
import dev.devbrew.keykeep.database.APIKeysTable
import dev.devbrew.keykeep.database.IPAddress
import dev.devbrew.keykeep.database.IPAddressesTable
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ValidationResponse(val isValid: Boolean, val error: String? = null)

fun Routing.apiKeyValidationRoute() {
    get("/validate") {
        val apiKeyParam = call.request.queryParameters["apiKey"]
        val ipParam = call.request.queryParameters["ip"]
        if (apiKeyParam != null && ipParam != null) {
            val response = transaction {
                val apiKeyEntity = APIKey.find { APIKeysTable.apiKey eq apiKeyParam }.singleOrNull()
                if (apiKeyEntity != null) {
                    val currentIpCount = IPAddressesTable.select {
                        IPAddressesTable.apiKeyId eq apiKeyEntity.id
                    }.count()
                    if (currentIpCount < apiKeyEntity.allowedIPs) {
                        val ipAddressExists = IPAddress.find {
                            IPAddressesTable.apiKeyId eq apiKeyEntity.id and (IPAddressesTable.ipAddress eq ipParam)
                        }.singleOrNull() != null
                        if (!ipAddressExists) {
                            IPAddress.new {
                                apiKeyId = apiKeyEntity
                                ipAddress = ipParam
                            }
                        }
                        ValidationResponse(isValid = true)
                    } else {
                        ValidationResponse(isValid = false, error = "IP address limit exceeded")
                    }
                } else {
                    ValidationResponse(isValid = false, error = "Invalid API key")
                }
            }
            call.respond(response)
        } else {
            call.respond(ValidationResponse(isValid = false, error = "API Key and/or IP parameter is missing"))
        }
    }
}
