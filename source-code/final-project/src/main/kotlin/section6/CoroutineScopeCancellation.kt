package org.example.section6

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

fun main() = runBlocking {
    log("main runBlocking     ")

    val scope = CoroutineScope(SupervisorJob())

    val job2 = scope.launch {
        log("    job1 launched")
        //throw Exception()
    }

    val job = scope.launch(Dispatchers.Default) {
        log("    job2 launched      ")
        fireOffIsolatedScopeTask()

        val task1 = launch {
            log("        task1 launch    ")
            delay(2000) // simulate a background task
            log("        task1 complete   ")
        }

        val task2 = launch {
            log("        task2 launch    ")
        }
    }.join()


    log("Start job            ")
    log("Waiting and delay to start job work...")
    delay(1000)
    log("Program ends         ")
}

private suspend fun fireOffIsolatedScopeTask() = coroutineScope {
    log("new scope launched")

    val task2 = launch {
        log("    task2 launch    ")
        delay(4000) // simulate a background task

        val task3 = launch {
            log("        task3 launch      ")
            log("        task3 complete    ")
            throw Exception("unhandled")
        }

        val task4 = launch {
            log("        task4 launch      ")
            log("        task4 complete")
        }
        log("    task2 complete")
    }.join()

    launch { log("task5") }
    log("end of scope")
}