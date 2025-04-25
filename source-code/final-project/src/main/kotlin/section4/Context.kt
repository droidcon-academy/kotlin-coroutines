package org.example.section4

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * From https://github.com/Kotlin/kotlinx.coroutines/issues/143
 *
 * Coroutine context behaves like a map of context elements
 *
 * fun main(args: Array<String>) {
 *     println(mapOf(1 to "A") + mapOf(2 to "B")) // {1=A, 2=B}
 *     println(mapOf(1 to "A") + mapOf(1 to "B")) // {1=B}
 *     println(mapOf(1 to "B") + mapOf(1 to "A")) // {1=A}
 * }
 *
 * elements to the right of + overwrite elements. Context is basically like a map
 */

fun main() = runBlocking {
    log("main runBlocking       ")

    val task1 = launch(Dispatchers.Default) {
        log("   task1 launched      ")
        delay(1000) // simulate a background task
        log("   task1 context switch")
        log("   task1 complete      ")
    }

    log("Start job              ")
    task1.join()
    log("Program ends           ")
}