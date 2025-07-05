package org.example.section3

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * This example demonstrates how launch returns a `Job` type.
 *
 * Execution order:
 *
 * 1. `main()` starts with runBlocking, which blocks the main thread and provides a coroutine scope.
 * 2. A job is launched inside that scope.
 * 3. Inside the job, two subtasks are created:
 *    - `task1`: a coroutine launched with `launch` - asynchronous, non-blocking.
 *    - `task2`: a coroutine-like block using `runBlocking` - synchronous, blocking.
 * 4. The program logs events before, during, and after `task1` and `task2`.
 * 5. `job.join()` ensures the main thread waits for job and its child coroutines to complete before ending.
 */
fun main() = runBlocking {
    log("main runBlocking     ")
    log("Start job            ")

    val job = launch {
        log("    job launched    ")
        val task1 = launch {
            log("    task1 runBlocking")
            //cancel()
            delay(1000) // simulate a background task
            log("    task1 complete   ")
        }

        val task2 = runBlocking {
            log("    task2 runBlocking")
            delay(1000) // simulate a background task
            log("    task2 complete   ")
        }
    }
    job.join()
    log("Program ends         ")
}

