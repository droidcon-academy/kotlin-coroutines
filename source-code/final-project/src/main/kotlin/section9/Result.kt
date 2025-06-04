package org.example.section9

/**
 * A sealed class that represents the outcome of an operation, either a [Success] or a [Failure].
 *
 * @param R The type of the successful result.
 */
sealed class Result<out R> {

    /**
     * Represents a successful result of an operation.
     *
     * @param T The type of the successful data.
     * @property data The result data returned from the operation.
     */
    data class Success<out T>(val data: T): Result<T>()

    /**
     * Represents a failed result of an operation.
     *
     * @property error The exception or error that caused the failure. Can be null if unknown.
     */
    data class Failure(val error: Throwable?): Result<Nothing>()
}