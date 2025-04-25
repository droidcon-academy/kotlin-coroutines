package org.example.section2

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

fun main(): Unit = runBlocking {
    log("main runBlocking    ")

    launch {
        log("launch started ")
        val result = doSomeHeavyWork()
        log(result)
        log ("launch finished. $result")
    }
}

suspend fun doSomeHeavyWork(): String {
    log("doSomeHeavyWork     ")
    delay(1000)
    return "heavyWork complete "
}