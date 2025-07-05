package org.example.section2

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * Shows how a suspending function can delay without blocking.
 *
 * Execution order:
 * 1. The `main` function calls `runBlocking`, which creates a coroutine scope and blocks the main thread
 *    until all coroutines inside complete.
 * 2. Inside `runBlocking`, a new coroutine is launched using `launch`.
 * 3. This coroutine calls the suspending function `doSomeHeavyWork()`.
 * 4. `doSomeHeavyWork()` logs its start, suspends for 1 second with `delay()`, then returns a result string.
 * 5. The launched coroutine logs the returned result and finishes.
 * 6. After all coroutines complete, `runBlocking` returns, and the program ends.
 */
fun main(): Unit {
    runBlocking {
        log("main runBlocking    ")

        launch {
            log("launch started ")
            val result = doSomeHeavyWork()  // simulates heavy work
            log(result)
            log ("launch finished. $result")
        }
    }
}

/**
 * A suspending function that simulates some heavy work by delaying 1 second.
 * Returns a completion message.
 */
suspend fun doSomeHeavyWork(): String {
    log("doSomeHeavyWork     ")
    delay(1000)
    return "heavyWork complete "
}