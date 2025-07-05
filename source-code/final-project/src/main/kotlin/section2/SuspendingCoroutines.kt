package org.example.section2

import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * `launch` starts a coroutine for concurrent work without returning a result. `task1` may cancel before logging
 * the result.
 *
 * Execution order:
 * 1. The `main` function starts a `runBlocking` coroutine scope, blocking the main thread.
 * 2. Inside `runBlocking`, `task1` coroutine is launched and immediately logs its start.
 * 3. `task1` calculates a result and logs it, but before it can finish, it is canceled.
 * 4. `task2` coroutine is launched next and runs to completion, logging its start and the computed result.
 * 5. Finally, the program logs that it completes.
 */
fun main() = runBlocking  {
    log("main                         ")

    val task1 = launch {
        log("task1 launched              ")
        val result = 1 + 2 + 3
        log ("task1 finished. Result: $result")
    }

    task1.cancel()

    val task2 = launch {
        log("task2 launched              ")
        val result = 1 + 2
        log ("task2 finished. Result: $result")
    }
    log("program completes              ")
}
