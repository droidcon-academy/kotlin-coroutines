package org.example.section2

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * Shows that `runBlocking` can be nested but blocks the thread each time.
 *
 * Execution order:
 * 1. The `main` function starts a `runBlocking` coroutine scope, blocking the main thread.
 * 2. Inside `main`’s `runBlocking`, another nested `runBlocking` is called.
 * 3. The inner `runBlocking` logs its start, delays for 2 seconds, computes a simple sum, then logs the result.
 * 4. After the inner `runBlocking` completes, control returns to the outer `runBlocking`.
 * 5. Finally, the program logs completion and terminates.
 */
fun main() = runBlocking  {
    log("main                         ")

    runBlocking {
        log("runBlocking launched              ")
        delay(2000)					 // <-- add here
        val result = 1 + 2 + 3
        log ("runBlocking finished. Result: $result")
    }
    log("program completes              ")
}
