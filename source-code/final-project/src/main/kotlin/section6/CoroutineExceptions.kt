package org.example.section6

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
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
 * This example shows how a failure in one child coroutine (`job1`) can
 * propagate all the way up to the scope, making other sibling coroutines
 * unable to launch.
 */
fun main() = runBlocking {
    log("main runBlocking     ")

    // Launch in an individual scope that is always active until it's closed or the program closes.
    val scope = CoroutineScope(Job())

    // Launch job1 that throws an exception
    val job1 = scope.launch {
        log("    job1 launched")
        throw Exception("job1 failed here!")    // This failure won't cancel siblings because of SupervisorJob
        log("    job1 complete")                // This line is never reached
    }

    // Launch job2 which itself launches two child coroutines (task1 and task2)
    val job2 = scope.launch {
        log("    job2 launched      ")
        val task1 = launch {
            log("        task1 launch    ")
            log("        task1 complete   ")
        }
        log("    creating task2      ")

        val task2 = launch {
            log("        task2 launch    ")
            log("        task2 complete   ")
        }
        log("    job2 complete      ")
    }.join()

    log("Is scope still active? ${scope.isActive}")
    if (scope.isActive) scope.cancel()

    log("Delay                ")
    delay(1000)      // Give some time for all coroutines to finish cleanup
    log("Program ends         ")
}

