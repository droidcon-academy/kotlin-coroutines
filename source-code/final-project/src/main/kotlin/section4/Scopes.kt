package org.example.section4

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log
import kotlin.coroutines.coroutineContext

fun main() = runBlocking {
    log("main runBlocking")

    val job = launch {
        log("    job launched")
        val parentScope = this

        coroutineScope {
            val task1 = async {
                log("    task1 launch")
                println(getJob(parentScope))
                delay(6000) // simulate a background task
                log("    task1 complete")
            }
        }
    }

    log("Start job    ")
    job.join()
    log("Program ends    ")
}

fun CoroutineScope.getJob(parentScope: CoroutineScope? = null): String {
   val scopeCheck = parentScope?.let { " | is the same scope? ${parentScope == this}\n" }  ?: ""
    return "\tScope: $this \tJob: ${coroutineContext[Job]}" + scopeCheck
}