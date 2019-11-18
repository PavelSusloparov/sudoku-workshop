package com.workshop.sudokubook.cucumber

import com.workshop.sudokubook.jdbc.buildTestDataSource
import cucumber.api.java.After
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.support.TransactionTemplate

class AfterTest {

    private val jdbc: JdbcTemplate
    private val tx: TransactionTemplate

    private var tables: List<String> = emptyList()

    init {
        val ds = buildTestDataSource()
        jdbc = JdbcTemplate(ds)
        val txMgr = DataSourceTransactionManager(ds)
        tx = TransactionTemplate(txMgr)
    }

    @After
    fun tearDown() {
        // clean up all tables via a transaction
        tx.execute {
            // collect all table names that are in the public schema
            if (tables.isEmpty()) {
                tables = jdbc.queryForList("""
                    SELECT TABLE_NAME
                    FROM INFORMATION_SCHEMA.TABLES
                    WHERE TABLE_SCHEMA = 'PUBLIC'
                """.trimIndent(), String::class.java)
            }

            // TRUNCATE all tables with restarting the IDENTITY sequence
            // and disregarding the foreign key constraint checks:
            // https://stackoverflow.com/a/21833815/9698467
            tables.forEach {
                jdbc.execute("TRUNCATE TABLE $it RESTART IDENTITY AND COMMIT NO CHECK")
            }
        }
    }
}
