package org.example.section6

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.TimeoutCancellationException
import org.example.log

fun main() = runBlocking {
    log("main runBlocking     ")
    try {
        withTimeout(1500) {
            log("    job launched     ")
            launchTask(1).join()
            launchTask(2).join()
            launchTask(3).join()
            launchTask(4).join()
        }
    } catch (e: TimeoutCancellationException) {
        log("TimeoutCancellationException: $e")
    }

    log("Delay                 ")
    delay(1500)
    log("Program ends         ")
}

private fun CoroutineScope.launchTask(number: Int): Job = launch {
    log("    task $number launched")
    delay(500)
}
