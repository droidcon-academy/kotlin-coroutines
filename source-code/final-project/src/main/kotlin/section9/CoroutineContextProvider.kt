package org.example.section9

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class CoroutineContextProviderImpl() : CoroutineContextProvider {
    override val defaultDispatcher: CoroutineContext by lazy { Dispatchers.Default }
    override val ioDispatcher: CoroutineContext by lazy { Dispatchers.IO }
}

interface CoroutineContextProvider {
    val defaultDispatcher: CoroutineContext
    val ioDispatcher: CoroutineContext
}