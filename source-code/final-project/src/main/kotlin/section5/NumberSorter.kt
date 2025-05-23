package org.example.section5

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.example.log
import kotlin.random.Random
import kotlin.system.measureTimeMillis

/**
 * Generates a randomized data set and sorts the large data
 * in a background context.
 *
 * Use Dispatchers.Default to offload computationally expensive
 * operations, like photo editing, complex math computations, etc.
 */
fun main() = runBlocking {
    // starts all coroutines in the background
    val scope = CoroutineScope(Dispatchers.Default)

    log("Creating list...")
    val numbers = List<Int>(10000000) { Random.nextInt(0, 10000000) }

    val time = measureTimeMillis {              // measures time for performance analysis
        val sortedNumbers = scope.async {
            log("Sorting...")
            numbers.sorted()
        }.await()

        log("First 10 sorted numbers: ${sortedNumbers.take(100)}")
    }

    log("Sorting took $time ms")
}