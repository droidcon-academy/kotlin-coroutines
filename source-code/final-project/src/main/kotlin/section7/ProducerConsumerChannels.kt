package org.example.section7

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * Entry point of the program that simulates a checkout lane using Kotlin coroutines and channels.
 *
 * The program:
 * - Creates a list of shoppers
 * - Sends them through a coroutine-backed channel with limited capacity (to simulate backpressure)
 * - Collects the order in which shoppers are received and logs progress
 */
fun main(): Unit = runBlocking {

    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("Ren", 3),
    )
    val checkoutLane: ReceiveChannel<Shopper> = checkoutLane(shoppers) // does it not need to close?
    val orderOfCheckout = mutableListOf<Shopper>()

    checkoutLane.consumeEach { shopper ->
        orderOfCheckout.add(shopper)
        log("curr order in parent      : ${orderOfCheckout.map { it.name }}")
    }

    println("final order: ${orderOfCheckout.map { it.name }}")
}

/**
 * Creates a producer coroutine that sends shoppers through a buffered channel.
 *
 * @param shoppers The list of shoppers to process.
 * @return A [ReceiveChannel] of [Shopper] to be consumed.
 *
 * This uses a fixed-capacity channel (`capacity = 2`) to simulate a limited number of
 * processing slots at the checkout. If the buffer is full, `send()` suspends until space frees up.
 */
@OptIn(ExperimentalCoroutinesApi::class)
private fun CoroutineScope.checkoutLane(
    shoppers: List<Shopper>
): ReceiveChannel<Shopper> = produce(capacity = 2) {		// <-- backpressure here
    shoppers.forEach { shopper ->
        delay(100)						                    // <-- suspends if buffer is full
        send(shopper)
    }
}


