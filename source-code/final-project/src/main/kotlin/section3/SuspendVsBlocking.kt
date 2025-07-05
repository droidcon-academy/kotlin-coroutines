package org.example.section3

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * This example illustrates the difference between:
 *
 * - Concurrency with suspending functions, which are cooperative and do not block threads.
 * - Blocking behavior using runBlocking, which waits for launched coroutines if explicitly joined.
 * - Partial synchronization, where only one of the launched tasks is awaited.
 *
 * Execution order:
 *
 * 1. The program starts with `runBlocking`, which blocks the main thread and provides a coroutine scope.
 * 2. Two coroutines, `task1` and `task2` are launched concurrently:
 *    - `task1` starts, delays for 1 second, then logs its completion.
 *    - `task2` starts, delays for 1 second, then logs its completion.
 * 3. `.join()` is called immediately after launching `task2`, so the main coroutine waits for `task2` to complete.
 * 4. Once task2 completes, the main coroutine proceeds to log "Program ends".
 * 5. task1 may still be running unless its delay finishes first—its completion is not awaited explicitly.
 */
fun main() = runBlocking {
    log("main runBlocking")

    val task1 = launch {
        log("    task1 launch")
        delay(1000) // simulate a background task
        log("    task1 complete ")
    }

    val task2 = launch {
        log("    task2 launch")
        delay(1000) // simulate a background task
        log("    task2 complete")
    }.join()

    log("Program ends")
}

