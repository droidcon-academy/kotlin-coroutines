package org.example.section3

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

fun main() = runBlocking {
    log("main runBlocking     ")
    log("Start job            ")

    val job = launch {
        log("    job launched    ")
        val task1 = launch {
            log("    task1 runBlocking")
            //cancel()
            delay(1000) // simulate a background task
            log("    task1 complete   ")
        }
         // <--- add here

        val task2 = runBlocking {
            log("    task2 runBlocking")
            delay(1000) // simulate a background task
            log("    task2 complete   ")
        }
    }
    job.join()
    log("Program ends         ")
}

