package org.example.section4

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log
import kotlin.coroutines.coroutineContext

fun main() = runBlocking {
    log("main runBlocking     ${getJob()}")

    val job = launch {
        log("    job launched      ${getJob()}")
        val parentScope = this
        fireOffIsolatedTask(this)

        val task1 = launch {
            log("    task1 launch    ${getJob()}")
            delay(6000) // simulate a background task
            log("    task1 complete   ${getJob()}")
        }
    }

    log("Start job            ${getJob()}")
    job.join()
    log("Program ends         ${getJob()}")
}
private suspend fun fireOffIsolatedTask(parentScope: CoroutineScope) = coroutineScope {
    println("same scope? ${this == parentScope}")

    log("new scope launched")

    val task2 = launch { // launch(isolatedJob) {
        log("    task2 launch    ${getJob()}")
        cancel()
        delay(4000) // simulate a background task

        val task3 = launch {
            log("        task3 launch      ${getJob()}")
            log("        task3 complete    ${getJob()}")
        }

        val task4 = launch {
            log("        task4 launch      ${getJob()}")
            log("        task4 complete    ${getJob()}")
        }
        log("    task2 complete   ${getJob()}")
    }

    launch { log("task5") }
    log("end of scope")
}

fun CoroutineScope.getJob(): String {
    return "Scope: $this | Job: ${coroutineContext[Job]}"
}

suspend fun scopeCheck(parentScope: CoroutineScope, scope: CoroutineScope) {
    println("\nparent scope: $parentScope | parent coroutineContext: ${parentScope.coroutineContext}")
    println("current scope: $scope | current coroutineContext: $coroutineContext")
    println("is the same scope? ${parentScope === scope}\n")
}