package dev.devbrew.keykeep.services

import dev.devbrew.keykeep.database.APIKeysTable
import dev.devbrew.keykeep.database.CustomersTable
import dev.devbrew.keykeep.database.IPAddressesTable
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


object DatabaseService {
    private val dotenv = dotenv {
        systemProperties = true
    }

    fun init() {
        val connectionUrl = dotenv["CONNECTION_URL"]
        val database = Database.connect(
            url = connectionUrl
        )
        //init tables if not present
        transaction {
            SchemaUtils.createMissingTablesAndColumns(CustomersTable, APIKeysTable, IPAddressesTable)
        }
    }
}