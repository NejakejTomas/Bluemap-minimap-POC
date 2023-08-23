package cz.nejakejtomas.bluemapminimap.dbs

import cz.nejakejtomas.bluemapminimap.dbs.tables.ServerTable
import cz.nejakejtomas.bluemapminimap.dbs.tables.WorldTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

object Database {
    private val database: Database

    init {
        val location = Path("config/bluemapminimap")
        location.createDirectories()

        database = Database.connect(
            "jdbc:sqlite:file:$location/dbs.sqlite",
            driver = "org.sqlite.JDBC"
        )

        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(
                ServerTable,
                WorldTable,
            )
        }
    }

    fun <T> transaction(statement: Transaction.() -> T): T = transaction(database) {
        statement()
    }
}