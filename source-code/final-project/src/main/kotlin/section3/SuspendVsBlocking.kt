package org.example.section3

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

fun main() = runBlocking {
    log("main runBlocking")

    val task1 = launch {
        log("    task1 launch")
        delay(1000) // simulate a background task
        log("    task1 complete ")
    }

    val task2 = launch {
        log("    task2 launch")
        delay(1000) // simulate a background task
        log("    task2 complete")
    }.join()

    log("Program ends")
}

