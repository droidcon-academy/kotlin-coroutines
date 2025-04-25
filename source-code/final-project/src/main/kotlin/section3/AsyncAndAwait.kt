package org.example.section3

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

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
