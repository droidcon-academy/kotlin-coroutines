package org.example.section4

import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * `async` is used when you need a result. `launch` is used for fire-and-forget side-effectful work.
 * Cancelling a Job (task1) doesn't affect sibling jobs (task2) or the parent async.
 *
 * Execution order:
 *
 * 1. `runBlocking` starts the main coroutine.
 * 2. Inside, an async job is launched, which:
 *      - Logs "job launched".
 *      - Launches `task1`, logs its launch.
 *      - Immediately cancels task.
 *      - Launches `task2`, logs its launch.
 *      - Returns "job completed" as a result.
 * 3. `job.await()` suspends until the async block completes, then logs "job completed".
 * 4. A new coroutine `job2` is launched, logs "job2 launched".
 * 5. Final log: "Program ends".
 */
fun main(): Unit = runBlocking {
    log("runBlocking launched    ")

    val job = async {
        log("    job launched     ")
        val task1 = launch {
            log("        task1 launched  ")
        }
        task1.cancel("        task1 canceled")

        val task2 = launch {
            log("        task2 launched  ")
        }
        "    job completed            "
    }
    log(job.await())

    val job2 = launch {
        log("    job2 launched")
    }

    log("Program ends         ")
}
