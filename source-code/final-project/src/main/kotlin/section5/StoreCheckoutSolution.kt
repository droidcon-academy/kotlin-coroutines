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

@OptIn(ExperimentalCoroutinesApi::class)
fun main(): Unit = runBlocking {

    val time = measureTimeMillis {

        val openCheckoutLanes = launch(Dispatchers.Default) {
            val shoppers = listOf(
                async { checkoutShopper("Jake", 3) },
                async { checkoutShopper("Zubin", 10) },
                async { checkoutShopper("Amber", 4) },
                async { checkoutShopper("Ren", 3) }
            )

            shoppers.awaitAll().forEach {
                println("    $it is checked out!")
            }
        }

        openCheckoutLanes.join()
    }

    println("Shoppers have been checked out. Time: ${time/1000.0} seconds")
}

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

private fun heavyWorkForProcessingInventory() = BigInteger.probablePrime(4096, Random())