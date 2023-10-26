package dev.devbrew.keykeep.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object CustomersTable : IntIdTable() {
    val name = varchar("name", length = 50)
}

class Customer(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Customer>(CustomersTable)

    var name by CustomersTable.name
}

object APIKeysTable : IntIdTable() {
    val customerId = reference("customer_id", CustomersTable)
    val apiKey = varchar("api_key", length = 50).uniqueIndex()
    val allowedIPs = integer("allowed_ips")
}

class APIKey(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<APIKey>(APIKeysTable)

    var customerId by Customer referencedOn APIKeysTable.customerId
    var apiKey by APIKeysTable.apiKey
    var allowedIPs by APIKeysTable.allowedIPs
}

object IPAddressesTable : IntIdTable() {
    val apiKeyId = reference("api_key_id", APIKeysTable)
    val ipAddress = varchar("ip_address", length = 50)
}

class IPAddress(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<IPAddress>(IPAddressesTable)

    var apiKeyId by APIKey referencedOn IPAddressesTable.apiKeyId
    var ipAddress by IPAddressesTable.ipAddress
}
