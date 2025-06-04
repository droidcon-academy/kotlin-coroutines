package org.example.section9

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Default implementation of [CoroutineContextProvider] that uses standard [Dispatchers].
 *
 * Uses lazy initialization to avoid unnecessary allocation until the dispatcher is first accessed.
 */
class CoroutineContextProviderImpl() : CoroutineContextProvider {
    override val defaultDispatcher: CoroutineContext by lazy { Dispatchers.Default }
    override val ioDispatcher: CoroutineContext by lazy { Dispatchers.IO }
}

/**
 * An interface that provides coroutine dispatchers.
 *
 * This abstraction allows for easier testing and swapping of dispatchers (e.g., replacing with TestDispatchers).
 */
interface CoroutineContextProvider {
    // Dispatcher optimized for CPU-intensive work.
    val defaultDispatcher: CoroutineContext
    // Dispatcher optimized for I/O operations such as network or database calls.
    val ioDispatcher: CoroutineContext
}