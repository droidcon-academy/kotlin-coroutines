package org.example.section5

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.example.log
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val scope = CoroutineScope(Dispatchers.Default)

    log("Creating list...")
    val numbers = List<Int>(10000000) { Random.nextInt(0, 10000000) }

    val time = measureTimeMillis {
        val sortedNumbers = scope.async {
            log("Sorting...")
            numbers.sorted()
        }.await()

        log("First 10 sorted numbers: ${sortedNumbers.take(100)}")
    }

    log("Sorting took $time ms")
}