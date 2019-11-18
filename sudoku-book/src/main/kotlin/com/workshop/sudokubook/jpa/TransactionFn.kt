package com.workshop.sudokubook.jpa

import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.support.DefaultTransactionDefinition

/**
 * Spring transaction management helpers.
 */

/**
 * Creates a transaction definition with the specified propagation.
 */
fun txDefinition(propagation: Propagation) =
        DefaultTransactionDefinition(propagation.value())
