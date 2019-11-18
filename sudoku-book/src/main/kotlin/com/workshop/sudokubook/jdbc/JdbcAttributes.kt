package com.workshop.sudokubook.jdbc

/**
 * Jdbc attributes - JDBC url, username, password, etc.
 */
interface JdbcAttributes {
    val url: String

    val username: String

    val password: String

    val driverClassName: String
}

data class JdbcAttributesImpl(
    override val url: String,
    override val username: String,
    override val password: String,
    override val driverClassName: String
) : JdbcAttributes

fun makeTestJdbcAttributes(dbname: String = "testdb") = JdbcAttributesImpl(
        url = "jdbc:hsqldb:mem:$dbname;sql.syntax_mys=true",
        username = "sa",
        password = "sa",
        driverClassName = "org.hsqldb.jdbc.JDBCDriver")

fun makeMysqlJdbcAttributes(host: String, dbname: String, username: String, password: String, port: Int = 3306) = JdbcAttributesImpl(
        url = "jdbc:mysql://$host:$port/$dbname?useSSL=false&nullNamePatternMatchesAll=true",
        username = username,
        password = password,
        driverClassName = "com.mysql.cj.jdbc.Driver"
)

val TestJdbcAttributes = makeTestJdbcAttributes()
