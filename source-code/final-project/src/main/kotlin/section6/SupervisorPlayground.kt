package org.example.section6

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import org.example.log

fun main() = runBlocking {
    log("main runBlocking     ")

    val scope = CoroutineScope(SupervisorJob())

    val job1 = scope.launch(Dispatchers.Default) {
        supervisorScope {
            log("    job1 launched      ")
            val task1 = launch {
                log("        task1 launch    ")
                throw Exception("task1 failed here!")
                log("        task1 complete   ")
            }
            log("    creating task2      ")

            val task2 = launch {
                log("        task2 launch    ")
                log("        task2 complete   ")
            }
            log("    job1 complete      ")
       }
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
