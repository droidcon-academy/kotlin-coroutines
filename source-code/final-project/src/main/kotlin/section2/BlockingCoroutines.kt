package org.example.section2

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.example.log

fun main() = runBlocking  {
    log("main                         ")

    runBlocking {
        log("runBlocking launched              ")
        delay(2000)					 // <-- add here
        val result = 1 + 2 + 3
        log ("runBlocking finished. Result: $result")
    }
    log("program completes              ")
}
