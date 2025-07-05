package org.example.section3

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * `async` 	- Launches a coroutine that returns a result. Runs concurrently. Also returns a `Job` like `launch`.
 * `await()` - Suspends the calling coroutine until the async task finishes and returns a value.
 *
 * Execution Order:
 *
 * 1. The `main()` function uses runBlocking to provide a coroutine scope and block the main thread until all
 *    coroutines complete.
 * 2. Two concurrent background tasks are launched using `async`, which allows them to run in parallel and return results.
 * 3. The program then uses `await()` to suspend until both `async` tasks finish.
 * 4. Results are printed and the program ends.
 */
fun main() = runBlocking {
    log("main runBlocking	")
    log("Start job")

    val job: Job = launch {
        val task1 = launch {
            log("    task1")
            delay(1000)
            log("    task1 complete ")
        }

        val task2: Deferred<String> = async {
            log("    task2")
            delay(1000)
            log("    task2 complete")
            "    task2 returned"
        }

        println("    task2 status: $task2")
        log(task2.await())
        println("    task2 status: $task2")
    }
    job.join()
    log("Program ends")
}
