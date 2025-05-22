package section9

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.example.section9.CoroutineContextProvider
import kotlin.coroutines.CoroutineContext


/**
 *
 */
class TestDispatcherProvider(scheduler: TestCoroutineScheduler): CoroutineContextProvider {
    override val defaultDispatcher: CoroutineContext by lazy { StandardTestDispatcher(scheduler) }
    override val ioDispatcher: CoroutineContext by lazy {  StandardTestDispatcher(scheduler) }
}

/**
 *
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UnconfinedTestDispatcherProvider(scheduler: TestCoroutineScheduler): CoroutineContextProvider {
    override val defaultDispatcher: CoroutineContext by lazy { UnconfinedTestDispatcher(scheduler) }
    override val ioDispatcher: CoroutineContext by lazy { UnconfinedTestDispatcher(scheduler) }
}