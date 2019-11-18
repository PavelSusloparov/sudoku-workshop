package com.workshop.sudokubook.jpa

import org.springframework.orm.jpa.EntityManagerHolder
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

/**
 * IoC helper for working with EntityManager instances.  Like TransactionTemplate, except it creates an EntityManager
 * that can be used with multiple transactions.  Integrates with Spring's transaction management framework.
 */
class EntityManagerTemplate(private val entityManagerFactory: EntityManagerFactory) {

    private class State {
        var level: Int = 0
    }

    /**
     * Executes the action with a single, multi-transaction persistence context.
     * Closes and un-registers the EntityManager once the action is complete.
     *
     * @param action The action
     * @param <T>    the return type
     * @return the return value from the action
    </T> */
    fun <T> execute(action: (EntityManager) -> T): T {
        var em: EntityManager? = null
        var cleanUp = false

        // Set up the thread local.
        if (STATE_THREAD_LOCAL.get() == null)
            STATE_THREAD_LOCAL.set(State())
        val state = STATE_THREAD_LOCAL.get()

        try {
            var entityManagerHolder: EntityManagerHolder? =
                    TransactionSynchronizationManager.getResource(entityManagerFactory) as EntityManagerHolder?
            if (entityManagerHolder == null) {
                em = entityManagerFactory.createEntityManager()
                assert(em != null) { "entityManagerFactory.createEntityManager() returned null!" }
                cleanUp = true
                // Since we're not in a transaction, the EntityManagerHolder ref should be null.
                // Put an EntityManagerHolder in the transaction sync thread local so that JpaTransactionManager will find it.
                entityManagerHolder = EntityManagerHolder(em!!)
                TransactionSynchronizationManager.bindResource(entityManagerFactory, entityManagerHolder)
            } else {
                // Otherwise, just use the existing EM.
                em = entityManagerHolder.entityManager
                cleanUp = false
            }
            state.level++
            return action.invoke(em)
        } finally {
            state.level--
            if (state.level <= 0)
                STATE_THREAD_LOCAL.remove()

            if (cleanUp) {
                em?.close()
                TransactionSynchronizationManager.unbindResource(entityManagerFactory)
            }
        }
    }

    companion object {

        private val STATE_THREAD_LOCAL = ThreadLocal<State>()

        val level get() = STATE_THREAD_LOCAL.get()?.level ?: 0

        val isActive get() = level > 0
    }
}
