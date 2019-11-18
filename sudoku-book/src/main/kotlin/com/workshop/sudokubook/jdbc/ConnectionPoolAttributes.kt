package com.workshop.sudokubook.jdbc

/**
 * JDBC connection pool functions
 */

interface ConnectionPoolAttributes {
    val maximumPoolSize: Int

    val maxLifetime: Int

    val leakDetectionThreshold: Int

    val connectionTimeout: Int
}

object DefaultConnectionPoolAttributes : ConnectionPoolAttributes {
    override val maximumPoolSize: Int
        get() = 16
    override val maxLifetime: Int
        get() = 14400
    override val leakDetectionThreshold: Int
        get() = 60
    override val connectionTimeout: Int
        get() = 60
}