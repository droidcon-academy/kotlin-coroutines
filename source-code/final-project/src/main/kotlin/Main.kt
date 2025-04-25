package org.example

import kotlinx.coroutines.runBlocking

fun log(message: String) {
    println("$message    | current thread: ${Thread.currentThread().name}")
}

fun main() = runBlocking {
    log("runBlocking launched")
}
