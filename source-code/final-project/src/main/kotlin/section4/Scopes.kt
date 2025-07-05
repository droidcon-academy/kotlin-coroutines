package org.example.section4

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log
import kotlin.coroutines.coroutineContext

/**
 * This example demonstrates:
 * - The difference between coroutine scopes (CoroutineScope, coroutineScope, runBlocking, etc.).
 * - How to inspect and compare coroutine scopes using the context.
 * - That coroutineScope creates a new scope with a different Job from its parent.
 *
 * Execution order:
 *
 * 1. `runBlocking` begins, logging "main runBlocking".
 * 2. A coroutine `job` is launched within `runBlocking`.
 *    - Logs "job launched".
 *    - Captures this as parentScope (the outer CoroutineScope for job).
 * 3. Inside `job`, `coroutineScope { ... }` creates a new nested scope:
 *    - An `async` task is launched.
 *    - `task1` logs its launch, prints the result of `getJob(...)` and delays for 6 seconds.
 * 4. The program waits for `job.join()` to complete.
 * 5. Logs "Program ends".
 */
fun main() = runBlocking {
    log("main runBlocking")

    val job = launch {
        log("    job launched")
        val parentScope = this

        coroutineScope {
            val task1 = async {
                log("    task1 launch")
                println(getJob(parentScope))
                delay(6000) // simulate a background task
                log("    task1 complete")
            }
        }
    }

    log("Start job    ")
    job.join()
    log("Program ends    ")
}

/**
 * Helper function to inspect the current CoroutineScope and its Job.
 * Optionally compares with a parent scope.
 */
fun CoroutineScope.getJob(parentScope: CoroutineScope? = null): String {
   val scopeCheck = parentScope?.let { " | is the same scope? ${parentScope == this}\n" }  ?: ""
    return "\tScope: $this \tJob: ${coroutineContext[Job]}" + scopeCheck
}