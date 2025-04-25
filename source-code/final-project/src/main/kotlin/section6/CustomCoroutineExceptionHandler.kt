package org.example.section6

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import org.example.log

fun main(): Unit = runBlocking {

    val ceh = CoroutineExceptionHandler { _, e ->
        println("CEH caught unhandled exception $e")
    }

    log("main runBlocking     ")

    val scope = CoroutineScope(Dispatchers.Default + ceh)

    val job = scope.launch(ceh) {
            log("    job launched      ")

            val task1 = launch {
                    log("    task1 launch    ")
                    throw Exception("job failed task1")
                    delay(1000) // simulate a background task
                    log("    task1 complete   ")
            }.join()

            val task2 = launch {
                log("    task2 launch    ")
                delay(1000) // simulate a background task
                log("    task2 complete   ")
            }.join()
            log("   job finishes")
    }

    log("Start job            ")
    job.join()

    println("Is scope still active? ${scope.isActive}")

    scope.launch(Dispatchers.Default){
        log ("extra task here")
    }

    log("Program ends         ")
}

// explain failing CEH implementations: https://stackoverflow.com/questions/53576189/coroutineexceptionhandler-not-executed-when-provided-as-launch-context
fun f() = runBlocking {
    val eh = CoroutineExceptionHandler { _, e -> println("exception handler: $e") }
    val cs1 = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val j1 = cs1.launch(eh + CoroutineName("first"))  {
        println("launched")
        delay(1000)
        launch(eh) {
            println("launched again")
            throw RuntimeException("error again!")
        }.join()
    }

    println("joining j1")
    j1.join()

    val cs2 = CoroutineScope(Dispatchers.Default + eh)
    val j2 = cs2.launch(CoroutineName("second"))  {
        println("launched")
        delay(1000)
        throw RuntimeException("error!")
    }

    println("joining j2")
    j2.join()

    println("after join")
}

fun finalExercise() = runBlocking {

    val ceh = CoroutineExceptionHandler { _, e ->
        println("CEH caught unhandled exception $e")
    }

    log("main runBlocking     ")

    val scope = CoroutineScope(Job())

    val job = scope.launch(Dispatchers.Default) {
        supervisorScope {
            log("    job launched      ")

            //throw RuntimeException("    task1 failed exception")

            val task1 = launch {
                log("    task1 launch    ")
                throw RuntimeException("task1 failed exception")
                delay(6000) // simulate a background task
                log("    task1 complete   ")
            }

            val task2 = launch {
                log("    task2 launch    ")
                delay(6000) // simulate a background task
                log("    task2 complete   ")
            }
        }
    }

    log("Start job            ")
    job.join()
    log("Program ends         ")
}