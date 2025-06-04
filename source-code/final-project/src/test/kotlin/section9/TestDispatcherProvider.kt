package section9

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.example.section9.CoroutineContextProvider
import kotlin.coroutines.CoroutineContext


/**
 * This test implementation implements StandardTestDispatcher,whose scheduler is injected
 * from the testScheduler of runTest.
 *
 * Coroutines do not execute automatically in this mode - only you can advance the coroutines
 * execution.
 */
class TestDispatcherProvider(scheduler: TestCoroutineScheduler): CoroutineContextProvider {
    override val defaultDispatcher: CoroutineContext by lazy { StandardTestDispatcher(scheduler) }
    override val ioDispatcher: CoroutineContext by lazy {  StandardTestDispatcher(scheduler) }
}

/**
 * This test implementation implements UnconfinedTestDispatcher,whose scheduler is injected
 * from the testScheduler of runTest.
 *
 * Coroutines executes eagerly in this mode - this is best used for simple test setup.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UnconfinedTestDispatcherProvider(scheduler: TestCoroutineScheduler): CoroutineContextProvider {
    override val defaultDispatcher: CoroutineContext by lazy { UnconfinedTestDispatcher(scheduler) }
    override val ioDispatcher: CoroutineContext by lazy { UnconfinedTestDispatcher(scheduler) }
}
