package org.example.section6

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * Demonstrates exception handling in coroutines using SupervisorJob.
 *
 * This example shows how a failure in one child coroutine (`job1`)
 * does NOT cancel sibling coroutines (`job2`) when using a SupervisorJob.
 */
fun main() = runBlocking {
    log("main runBlocking     ")

    // Create a CoroutineScope with SupervisorJob, allowing children to fail independently
    val scope = CoroutineScope(SupervisorJob())	// <-- change here

    val job1 = scope.launch {
        log("    job1 launched")
        throw Exception("job1 failed here")
    }

    val job2 = scope.launch(Dispatchers.Default) {
        log("    job2 launched        ")
        val task1 = launch {
            log("       task1 launched     ")
            delay(2000)  // simulate background task
            log("        task1 complete    ")
        }.join()
        log("        creating task2    ")

        val task2 = launch {
            log("         task2 launched    ")
        }
        log("        job1 complete    ")
    }.join()

    log("Is scope still active? ${scope.isActive}")
    if (scope.isActive) scope.cancel()

    log("Start job                	")
    delay(1000)
    log("Program ends         	")
}
