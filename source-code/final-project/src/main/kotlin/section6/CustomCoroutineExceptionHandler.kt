package org.example.section6

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import org.example.log

fun main(): Unit = runBlocking {

    val ceh = CoroutineExceptionHandler { _, e ->
        println("CEH caught unhandled exception $e")
    }

    log("main runBlocking     ")
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val job = scope.launch(ceh) {
        supervisorScope {
            log("    job launched      ")

            // throw RuntimeException("    task1 failed exception")

            val task1 = launch {
                log("    task1 launch    ")
                throw RuntimeException("task1 failed exception")
                delay(6000) // simulate a background task
                log("    task1 complete   ")
            }

            val task2 = launch {
                log("    task2 launch    ")
                delay(6000) // simulate a background task
                log("    task2 complete   ")
            }
        }
    }

    log("Start job            ")
    job.join()
    log("Program ends         ")
}