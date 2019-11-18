package com.workshop.sudokubook.jpa

import com.workshop.sudokubook.jdbc.buildTestDataSource
import com.workshop.sudokubook.jdbc.createLiquibase
import com.workshop.sudokubook.jdbc.executeSqlScript
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.core.io.Resource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.Properties
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource
import kotlin.reflect.KClass

/**
 * A basic configuration for using JPA with Spring in standalone mode, without persistence.xml.
 * Can be used for testing JPA-centric code with an embedded DB (without starting Spring Boot).
 * Includes JpaBean, for convenience. Subclass this to specify:
 * <ol>
 *     <li>packagesToScan - The package names to scan for entities.</li>
 *     <li>properties - Hibernate properties.</li>
 *     <li>changeLog - The name of the Liquibase change log (optional)</li>
 *     <li>initSql - Initial SQL to load into the database (optional)</li>
 * <br>
 * See:
 * <ul>
 * <li>http://www.baeldung.com/spring-jpa-test-in-memory-database</li>
 * <li>http://www.baeldung.com/the-persistence-layer-with-spring-and-jpa</li>
 * </ul>
 */
@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
abstract class StandaloneJpaConfig(
    /**
     * Packages to scan for entities.
     */
    val packagesToScan: List<String>,
    /**
     * Liquibase change log name - if null, Liquibase will not be used.
     * Default is null.
     */
    val changeLog: String? = null
) {

    @Bean
    abstract fun dataSource(): DataSource

    val properties by lazy { additionalProperties(Properties()) }

    /**
     * Give sub-classes the opportunity to add more Hibernate properties.
     */
    protected fun additionalProperties(props: Properties): Properties {
        return props
    }

    /**
     * Optional db initialization SQL.   Executed before JPA starts.
     * For example: <code>ClassPathResource("schema.sql")</code>
     */
    protected val initSql: Resource? = null

    @Bean
    fun springLiquibase(ds: DataSource) = createLiquibase(ds, changeLog)

    /**
     * Override this to customize how the database is set up.
     * By default it executes the initSql script where it's necessary.
     */
    protected fun initializeDatabase(ds: DataSource) {
        runInitSql(ds)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun runInitSql(ds: DataSource) {
        executeSqlScript(ds, initSql)
    }

    @Bean
    fun localContainerEntityManagerFactory(dataSource: DataSource):
        LocalContainerEntityManagerFactoryBean {
        initializeDatabase(dataSource)
        // Here is the secret to avoiding persistence.xml...
        val emfBean = LocalContainerEntityManagerFactoryBean()
        emfBean.dataSource = dataSource
        emfBean.setPackagesToScan(*packagesToScan.toTypedArray())
        val vendorAdapter = HibernateJpaVendorAdapter()
        emfBean.jpaVendorAdapter = vendorAdapter
        emfBean.setJpaProperties(properties)
        return emfBean
    }

    @Bean
    fun transactionManager(emf: EntityManagerFactory): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = emf
        return transactionManager
    }

    @Bean
    fun jpaBean(): JpaBean {
        return JpaBean()
    }
}

const val DEFAULT_DBNAME = "testdb"

/**
 * A standalone JPA configuration, for testing with an embedded HSQLDB/MySQL.
 */
@Configuration
abstract class StandaloneTestJpaConfig(
    /**
     * Packages to scan for entities.
     */
    packagesToScan: List<String>,
    /**
     * Set to false to disable hbm2ddl schema generation (default is true).
     */
    val hbm2ddl: Boolean = true,
    /**
     * The name of the schema (database) to use in the in memory HSQLDB server.
     */
    val dbname: String = DEFAULT_DBNAME,
    /**
     * Liquibase change log name (optional, defaults to null)
     */
    changeLog: String? = null
)
    : StandaloneJpaConfig(packagesToScan = packagesToScan, changeLog = changeLog) {

    init {
        @Suppress("LeakingThis")
        if (hbm2ddl && !changeLog.isNullOrEmpty())
            throw IllegalArgumentException(
                "Specify either hbm2ddl=true, or changeLog (liquibase change log), not both!")
    }

    constructor(
        /**
         * An example entity that will be used to determine the packages to scan for entities.
         */
        entityClass: KClass<*>,
        /**
         * Set to false to disable hbm2ddl schema generation (default is true).
         */
        hbm2ddl: Boolean = true,
        /**
         * The name of the schema (database) to use in the in memory HSQLDB server.
         */
        dbname: String = DEFAULT_DBNAME,
        /**
         * Liquibase change log name (optional, defaults to null)
         */
        changeLog: String? = null
    ) : this(
        packagesToScan = packagesToScanFromClasses(entityClass),
        hbm2ddl = hbm2ddl,
        dbname = dbname,
        changeLog = changeLog)

    override fun dataSource(): DataSource = buildTestDataSource(dbname)

    override fun additionalProperties(props: Properties): Properties {
        if (hbm2ddl)
            hsqldbAutoSchema(props)
        else
            mysqlHsqldbProperties(props)
        return super.additionalProperties(props)
    }
}