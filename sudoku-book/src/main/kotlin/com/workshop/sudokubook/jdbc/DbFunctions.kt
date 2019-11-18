package com.workshop.sudokubook.jdbc

import liquibase.integration.spring.SpringLiquibase
import org.springframework.core.io.Resource
import org.springframework.jdbc.core.ConnectionCallback
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.init.ScriptUtils
import javax.sql.DataSource

/**
 * Database helper functions
 */

fun executeSqlScript(ds: DataSource, sql: Resource?) {
    if (sql != null && sql.exists()) { // Execute the init SQL script if it exists.
        val template = JdbcTemplate(ds, true)
        template.execute(ConnectionCallback { con ->
            ScriptUtils.executeSqlScript(con, sql)
        })
    }
}

fun createLiquibase(ds: DataSource, changeLog: String?): SpringLiquibase {
    return SpringLiquibase().also {
        if (changeLog != null) {
            it.changeLog = changeLog
            it.dataSource = ds
        } else {
            it.setShouldRun(false)
        }
    }
}
