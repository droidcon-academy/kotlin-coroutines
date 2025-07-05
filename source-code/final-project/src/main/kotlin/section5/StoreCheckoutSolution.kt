package org.example.section5

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log
import java.math.BigInteger
import java.util.Random
import kotlin.system.measureTimeMillis

/**
 * Optimized version of the StoreCheckoutProblem.kt.
 *
 * Instead of processing all shoppers sequentially, this version leverages `async` coroutines
 * on `Dispatchers.Default` to run checkout operations concurrently.
 *
 * It simulates each shopper's checkout including scanning items and heavy CPU-bound inventory processing.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun main(): Unit = runBlocking {

    val time = measureTimeMillis {

        // Launch a coroutine that opens checkout lanes
        val openCheckoutLanes = launch(Dispatchers.Default) {
            val shoppers = listOf(
                async { checkoutShopper("Jake", 3) },
                async { checkoutShopper("Zubin", 10) },
                async { checkoutShopper("Amber", 4) },
                async { checkoutShopper("Ren", 3) }
            )

            // Wait for all checkout tasks to finish
            shoppers.awaitAll().forEach {
                println("    $it is checked out!")
            }
        }

        openCheckoutLanes.join()
    }

    // Wait for all checkout lanes to complete
    println("Shoppers have been checked out. Time: ${time/1000.0} seconds")
}

/**
 * Simulates the checkout process for a single shopper.
 *
 * @param name The name of the shopper
 * @param numberOfItems The number of items being checked out
 * @return The shopper's name after successful checkout
 */
@OptIn(ExperimentalCoroutinesApi::class)
private fun CoroutineScope.checkoutShopper(name: String, numberOfItems: Int): String {
    log("Checking out $name.    ")
    log("    $name has $numberOfItems items. Checking out...")
    (1..numberOfItems).forEachIndexed { i, elem ->
        println("        item $elem scanned for $name.")
    }
    launch {
        heavyWorkForProcessingInventory()
    }

    return name
}

/**
 * Simulates a CPU-intensive task such as processing inventory updates.
 *
 * @return A large probable prime number, representing heavy computational work
 */
private fun heavyWorkForProcessingInventory() = BigInteger.probablePrime(4096, Random())