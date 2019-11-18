package com.workshop.sudokubook.jdbc

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

private const val MILLIS_PER_SECOND = 1000L

fun createHikariConfig(
    jdbcAttributes: JdbcAttributes,
    connectionPoolAttributes: ConnectionPoolAttributes
): HikariConfig {
    val config = HikariConfig()
    config.driverClassName = jdbcAttributes.driverClassName
    config.jdbcUrl = jdbcAttributes.url
    config.username = jdbcAttributes.username
    config.password = jdbcAttributes.password

    // maximum number of milliseconds that a client will wait for a connection from the pool
    config.connectionTimeout = connectionPoolAttributes.connectionTimeout * MILLIS_PER_SECOND

    // "We strongly recommend setting this value, and it should be at least 30 seconds less than any
    // database-level connection timeout."
    // Currently: connect_timeout=10s, interactive_timeout=28800s
    config.maxLifetime = connectionPoolAttributes.maxLifetime * MILLIS_PER_SECOND

    config.isAutoCommit = false // No autocommit.   Please!

    // Setting this allows the pool to shrink to this level.  Hikari recommends not setting this.
    // config.setMinimumIdle(1);  NOSONAR
    config.maximumPoolSize = connectionPoolAttributes.maximumPoolSize
    // Enable leak detection: If a connection is checked out for too long, it is suspect.
    config.leakDetectionThreshold = connectionPoolAttributes.leakDetectionThreshold * MILLIS_PER_SECOND

    // Connection test queries should be in their own transaction.
    config.isIsolateInternalQueries = true

    // Need 'read committed' because we don't want things like db-based locks to behave badly.
    config.transactionIsolation = "TRANSACTION_READ_COMMITTED"

    // If we don't set this, Hikari should try to use the JDBC4 connection test.
    // config.setConnectionTestQuery("SELECT 1");  NOSONAR
    config.connectionTestQuery = null
    return config
}

fun buildDataSource(
    jdbcAttributes: JdbcAttributes,
    connectionPoolAttributes: ConnectionPoolAttributes,
    metricRegistry: Any? = null,
    healthCheckRegistry: Any? = null
): HikariDataSource {
    val config = createHikariConfig(jdbcAttributes, connectionPoolAttributes)

    if (metricRegistry != null) {
        config.metricRegistry = metricRegistry
    }
    if (healthCheckRegistry != null) {
        config.healthCheckRegistry = healthCheckRegistry
    }
    return HikariDataSource(config)
}

fun buildTestDataSource(dbname: String = "testdb") =
        buildDataSource(makeTestJdbcAttributes(dbname), DefaultConnectionPoolAttributes)
