package org.example.section6

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.TimeoutCancellationException
import org.example.log

/**
 * Demonstrates coroutine cancellation using `withTimeout`.
 *
 * This example launches multiple tasks sequentially inside a `withTimeout` block,
 * which cancels the coroutine if it does not complete within the specified timeout.
 *
 * Each task simulates work by delaying 500 ms.
 * Because the total work exceeds the timeout, 1500 ms,
 * a [TimeoutCancellationException] is thrown and caught.
 */
fun main() = runBlocking {
    log("main runBlocking     ")
    try {
        withTimeout(1500) {
            log("    job launched     ")
            launchTask(1).join()
            launchTask(2).join()
            launchTask(3).join()
            launchTask(4).join()
        }
    } catch (e: TimeoutCancellationException) {
        log("TimeoutCancellationException: $e")
    }

    log("Delay                 ")
    delay(1500)
    log("Program ends         ")
}

/**
 * Launches a coroutine task that simulates work by delaying 500 ms.
 *
 * @param number The task number.
 * @return The [Job] representing the launched coroutine.
 */
private fun CoroutineScope.launchTask(number: Int): Job = launch {
    log("    task $number launched")
    delay(500)
}
