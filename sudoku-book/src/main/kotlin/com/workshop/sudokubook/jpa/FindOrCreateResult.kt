package com.workshop.sudokubook.jpa

/**
 * Result object for findOrCreate
 * @see JpaBean.findOrCreate
 */
sealed class FindOrCreateResult<T>(val entity: T) {
    class Created<T>(entity: T) : FindOrCreateResult<T>(entity)
    class Found<T>(entity: T) : FindOrCreateResult<T>(entity)
    class Retried<T>(entity: T, val exception: Throwable) : FindOrCreateResult<T>(entity)
}