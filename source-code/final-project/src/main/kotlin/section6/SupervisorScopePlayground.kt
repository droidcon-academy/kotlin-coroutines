package org.example.section6

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import org.example.log

/**
 * Demonstrates the behavior of supervisorScope and SupervisorJob
 * with nested coroutines and exception handling.
 *
 * Shows how exceptions in child coroutines do NOT cancel siblings inside supervisorScope,
 * and how the parent coroutine behaves with these exceptions.
 */
fun main() = runBlocking {
    log("main runBlocking     ")

    // Create a CoroutineScope with SupervisorJob, allowing children to fail independently
    val scope = CoroutineScope(SupervisorJob())

    val job1 = scope.launch {
        log("    job1 launched        ")

        // supervisorScope: child coroutines inside it can fail independently,
        // exceptions in one child do not cancel others
        supervisorScope {
            val task1 = launch {
                log("       task1 launched     ")
                throw Exception("job1 failed here")
                log("        task1 complete    ")
            }.join()

            log("        creating task2    ")

            val task2 = launch {
                log("         task2 launched    ")
                log("         task2 complete    ")
            }
        }
        log("        job1 complete    ")
    }.join()

    val job2 = scope.launch {
        log("    job2 launched")
        log("    job2 complete")
    }

    log("Is scope still active? ${scope.isActive}")
    if (scope.isActive) scope.cancel()

    log("Delay                ")
    delay(1000)
    log("Program ends         ")
}
