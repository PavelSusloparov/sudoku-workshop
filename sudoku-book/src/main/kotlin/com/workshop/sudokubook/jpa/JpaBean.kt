package com.workshop.sudokubook.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.springframework.transaction.support.TransactionTemplate
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.PersistenceContext
import kotlin.reflect.KClass

/**
 * Common JPA operations, including some simple programmatic transaction demarcation
 */
@Component
class JpaBean {

    @PersistenceContext
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var entityManagerFactory: EntityManagerFactory

    @Autowired
    private lateinit var txm: PlatformTransactionManager

    /**
     * Returns the ID of a given entity.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> entityId(entity: Any, idClass: KClass<T>): T? =
            entityManagerFactory.persistenceUnitUtil.getIdentifier(entity) as T?

    /**
     * Does em.find(), only useful to avoid injecting EntityManager.  :)
     */
    @Transactional
    fun <T, U> findById(entityClass: Class<T>, id: U): T? {
        return em.find(entityClass, id)
    }

    /**
     * @see JpaBean.findOneOrCreate
     * Returns the entity only
     */
    @Transactional
    fun <T> findOneOrCreate(
        entityClass: Class<T>,
        querySupplier: QuerySupplier<T>,
        entitySupplier: EntitySupplier<T>,
        postPersist: EntityPostProcessor<T> = { _, _ -> } // Default: do nothing.
    ): T = findOrCreate(entityClass, querySupplier, entitySupplier, postPersist).entity

    /**
     * Find or create a given object.  This should be used for normalization, as it will automatically retry
     * if the create fails (e.g. unique constraint violation).
     * @see com.wework.jpa.findOrCreate
     * @param entityClass The entity class.
     * @param querySupplier Function that supplies a JPA TypedQuery to find the entity.
     * @param entitySupplier Function that supplies a new, initialized entity for persisting.
     * @param postPersist Post process the entity, after it is persisted.  (Defaults to no-op)
     */
    @Transactional
    fun <T> findOrCreate(
        entityClass: Class<T>,
        querySupplier: QuerySupplier<T>,
        entitySupplier: EntitySupplier<T>,
        postPersist: EntityPostProcessor<T> = { _, _ -> } // Default: do nothing.
    ): FindOrCreateResult<T> =
        findOrCreate(em, txm, { findOne(entityClass, querySupplier) }, entitySupplier, postPersist)

    /**
     * Find or create operation, using em.find(SomeClass, id) as the 'find' query.
     * @param entityClass The entity class
     * @param id The primary key to find
     * @param entitySupplier Function that supplies a new, initialized entity for persisting.
     * @param postPersist Post process the entity, after it is persisted.  (Defaults to no-op)
     */
    @Transactional
    fun <T> findOrCreateById(
        entityClass: Class<T>,
        id: Any,
        entitySupplier: EntitySupplier<T>,
        postPersist: EntityPostProcessor<T> = { _, _ -> }
    ) =
        findOrCreate(em, txm,
            { em -> em.find(entityClass, id) },
            entitySupplier, postPersist)

    /**
     * Returns the first result of a query, or null.
     */
    @Transactional
    fun <T> findOne(entityClass: Class<T>, querySupplier: QuerySupplier<T>): T? =
        findOne(em, querySupplier)

    /**
     * Performs the action inside an extended persistence context.
     * @param action action to perform
     * @param <T> return type
     * @return return value from the action
     */
    fun <T> withExtendedPersistenceContext(action: (EntityManager) -> T): T =
        EntityManagerTemplate(entityManagerFactory).execute(action)

    /**
     * Performs the specified function inside a transaction with 'create if needed' transaction flow.
     * @see Propagation.REQUIRED
     * @param function the function
     * @param <T> the return type
     * @return the return value from the function
     */
    fun <T> withTx(function: (EntityManager) -> T): T? =
        txTemplate().execute { function(em) }

    /**
     * Creates a transaction template with the specified propagation.
     * @param propagation Transaction propagation type.
     */
    fun txTemplate(propagation: Propagation = Propagation.REQUIRED) =
            TransactionTemplate(txm, txDefinition(propagation))

    /**
     * Gets the current transaction status, with 'SUPPORTS' propagation (don't start a new one if there isn't any).
     */
    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    val transactionStatus get() = txm.getTransaction(txDefinition(Propagation.SUPPORTS))!!

    /**
     * Executes the action *outside* of a transaction IF we're inside an EntityManagerTemplate.
     * <ol>
     *     <li>Commit any transaction that is in progress.</li>
     *     <li>Execute (invoke) the action.</li>
     *     <li>If there was an existing transaction committed in step 1, create a new transaction.</li>
     * </ol>
     */
    fun <T> withoutTx(action: () -> T): T? {
        if (!EntityManagerTemplate.isActive)
            throw IllegalStateException("withoutTx() : Must be inside an extended persistence context.")

        var committed = false
        val tx = transactionStatus
        if (!tx.isCompleted && !tx.isRollbackOnly) {
            txm.commit(tx)
            committed = true
        }

        val rv: T?
        try {
            rv = action()
        } finally {
            // Start a new TX if we committed the old one.
            if (committed)
                txm.getTransaction(DefaultTransactionDefinition())
        }
        return rv
    }
}