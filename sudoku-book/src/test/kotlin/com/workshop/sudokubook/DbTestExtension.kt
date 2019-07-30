package com.workshop.sudokubook

import com.wework.jdbc.buildTestDataSource
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.support.TransactionTemplate

/**
 * This Extension is to be used on unit test classes that interact with the DB
 */
class DbTestExtension : SpringExtension() {

    private val jdbc: JdbcTemplate
    private val tx: TransactionTemplate

    private var tables: List<String> = emptyList()

    init {
        val ds = buildTestDataSource()
        jdbc = JdbcTemplate(ds)
        val txMgr = DataSourceTransactionManager(ds)
        tx = TransactionTemplate(txMgr)
    }

    /**
     * We need to clean up the DB tables after the execution of each DB-related
     * unit test.
     */
    override fun afterEach(context: ExtensionContext) {
        // first, execute the user-provided afterEach() logic which could
        // make assumptions about records still existing in the DB
        super.afterEach(context)

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
