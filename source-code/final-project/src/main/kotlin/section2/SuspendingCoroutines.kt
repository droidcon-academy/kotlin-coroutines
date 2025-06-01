package org.example.section2

import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

fun main() = runBlocking  {
    log("main                         ")

    val task1 = launch {
        log("task1 launched              ")
        val result = 1 + 2 + 3
        log ("task1 finished. Result: $result")
    }

    task1.cancel()

    val task2 = launch {
        log("task2 launched              ")
        val result = 1 + 2
        log ("task2 finished. Result: $result")
    }
    log("program completes              ")
}
