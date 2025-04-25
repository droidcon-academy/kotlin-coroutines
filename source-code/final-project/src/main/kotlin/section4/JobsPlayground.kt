package org.example.section4

import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

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
