package org.example.section5

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log
import java.math.BigInteger
import java.util.Random
import kotlin.system.measureTimeMillis

/**
 * Simulates a store checkout process using a single coroutine to handle multiple shoppers sequentially.
 * Demonstrates a performance bottleneck due to lack of parallelism when doing heavy computation.
 */
fun main(): Unit = runBlocking {

    val time = measureTimeMillis {
        launch {
            checkoutShopper("Jake", 3)
            checkoutShopper("Zubin", 10)
            checkoutShopper("Amber", 4)
            checkoutShopper("REE", 3)
        }.join()
    }

    log("Shoppers have checked out. Time: ${time/1000.0} seconds")
}

/**
 * Simulates the checkout process for a single shopper.
 *
 * @param name Name of the shopper
 * @param numberOfItems Number of items being purchased
 */
private fun checkoutShopper(name: String, numberOfItems: Int) {
    log("Checking out $name.    ")
    log("    $name has $numberOfItems items. Checking out...")
    (1..numberOfItems).forEachIndexed { i, elem ->
        println("        item $elem scanned for $name.")
    }
    val heavy = heavyWorkForProcessingInventory()
    log("heavy job finished: $heavy")
    log("    $name is checked out!")
}


/**
 * Simulates a CPU-intensive task such as processing inventory updates.
 *
 * @return A large probable prime number, representing heavy computational work
 */
private fun heavyWorkForProcessingInventory(): BigInteger = BigInteger.probablePrime(4096, Random())