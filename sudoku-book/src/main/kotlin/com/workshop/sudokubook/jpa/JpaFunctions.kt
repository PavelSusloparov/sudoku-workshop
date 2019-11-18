package com.workshop.sudokubook.jpa

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.support.TransactionTemplate
import java.util.Properties
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import kotlin.reflect.KClass

/**
 * JPA helper functions
 */

// --- JPA config functions ---

fun <T : Any> packagesToScanFromClasses(vararg classes: KClass<T>) =
        classes
                .map { it.java.`package`.name }
                .distinct().toList()

fun mysqlProperties(props: Properties) {
    props["hibernate.dialect"] = "org.hibernate.dialect.MySQL5Dialect"
    props["hibernate.id.new_generator_mappings"] = "false" // Use auto-increment by default.
}

// --- Embedded JPA testing utilities ---

/**
 * Sets JPA properties for using HSQLDB in mysql mode.
 * Use this from StandaloneJpaConfig.additionalProperties
 */
fun mysqlHsqldbProperties(props: Properties) {
    props["hibernate.id.new_generator_mappings"] = "false" // Use auto-increment by default.
    props["hibernate.dialect"] = HsqldbMysql5Dialect::class.java.name
}

/**
 * Sets up hbm2ddl auto schema generation and HSQLDB MySQL emulation.
 * Use this from StandaloneJpaConfig.additionalProperties
 */
fun hsqldbAutoSchema(props: Properties) {
    mysqlHsqldbProperties(props)
    props["hibernate.hbm2ddl.auto"] = "create-drop"
}

// --- JPA utility and extension functions ---

typealias QuerySupplier<T> = (EntityManager) -> TypedQuery<T>
typealias EntitySupplier<T> = (EntityManager) -> T
typealias FindFunction<T> = (EntityManager) -> T?
typealias EntityPostProcessor<T> = (EntityManager, T) -> Unit

/**
 * Returns the first query result if there is one and only one result.
 */
fun <T> TypedQuery<T>.oneOrNull() = resultList.let { if (it != null && it.size == 1) it[0] else null }

/**
 * Execute a query, return the first query result if there is one and only one result.
 */
fun <T> findOne(em: EntityManager, querySupplier: QuerySupplier<T>) =
    querySupplier(em).oneOrNull()

fun newTransaction(txm: PlatformTransactionManager) =
    TransactionTemplate(txm, txDefinition(Propagation.REQUIRES_NEW))

/**
 * Find or create a given object.  This should be used for normalization, as it will automatically retry
 * if the create fails (e.g. unique constraint violation).
 * <ol>
 *     <li>Look for the entity using the query supplied by the 'query supplier'</li>
 *     <li>If the entity is found, return it.   The result will have 'created = false`.</li>
 *     <li>If the entity was not found, suspend the current transaction, start a new transaction, and persist it.
 *     <b>NOTE: This will cause the current thread to have two connections from the connection pool temporarily!</b>
 *     </li>
 *     <li>If the new entity was persisted successfully, return it.   The result will have 'created = true`.
 *     The nested transaction will commit and the outer transaction will resume.</li>
 *     <li>If the new entity could not be persisted, the query will be re-tried.   This should succeed, assuming
 *     that another transaction has persisted the entity.  The result will have 'created = false', plus the exception
 *     that occurred while trying to persist.
 *     </li>
 * </ol>
 * @param em The EntityManager
 * @param txm Spring transaction manager
 * @param findFunction Function that finds the entity, either by executing a query or some other means.
 * @param createFunction Function that supplies a new, initialized entity for persisting.
 * @param postPersist Post process the entity, after it is persisted.  (Defaults to no-op)
 */
fun <T> findOrCreate(
    em: EntityManager,
    txm: PlatformTransactionManager,
    findFunction: FindFunction<T>,
    createFunction: EntitySupplier<T>,
    postPersist: EntityPostProcessor<T>
): FindOrCreateResult<T> =
    findFunction(em).let { found ->
        if (found != null)
            FindOrCreateResult.Found(found)
        else {
            try {
                newTransaction(txm).execute { // Start nested transaction.
                    val entity = createFunction(em)
                    em.persist(entity)
                    postPersist(em, entity)
                    FindOrCreateResult.Created(entity)
                }!! // End of nested transaction.
            } catch (t: Throwable) {
                // Something went wrong during the persist (nested tx will be rolled back by tx template)
                val otherTxCreated = findFunction.invoke(em) // Retry the query.
                    ?: throw IllegalStateException("Entity not found!")
                FindOrCreateResult.Retried(otherTxCreated, exception = t)
            }
        }
    }
