package section8

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.example.section8.CoroutineContextProvider
import kotlin.coroutines.CoroutineContext

class TestDispatcherProvider(
    override val defaultDispatcher: CoroutineContext = StandardTestDispatcher(),
    override val ioDispatcher: CoroutineContext = StandardTestDispatcher()
) : CoroutineContextProvider

class UnconfinedTestDispatcherProvider @OptIn(ExperimentalCoroutinesApi::class) constructor(
    override val defaultDispatcher: CoroutineContext = UnconfinedTestDispatcher(),
    override val ioDispatcher: CoroutineContext = UnconfinedTestDispatcher()
) : CoroutineContextProvider