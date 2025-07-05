package org.example.section7

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.log
import java.util.Collections

/**
 * Main function demonstrating a producer-consumer pattern using Kotlin coroutines and channels.
 *
 * - Creates multiple producer coroutines that simulate shoppers checking out.
 * - Sends shoppers through a [Channel] to a single consumer coroutine.
 * - The consumer records the order in which shoppers finish checkout.
 *
 * **Note:** The shared mutable list [orderOfCheckout] is accessed concurrently
 * without synchronization, which may cause race conditions in real-world scenarios.
 */
fun main(): Unit = runBlocking {

    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("Ren", 3),
    )
    val channel = Channel<Shopper>()
    val orderOfCheckout = mutableListOf<Shopper>()

    val producers = shoppers.map { shopper ->
        launch(Dispatchers.Default) {
            log("curr order in child launch: ${shopper.name}")
            channel.checkoutShopper(shopper)
        }
    }

    val consumer = launch(Dispatchers.Default) {
        for (i in channel) {
            orderOfCheckout.add(i)
            log("curr order in parent: ${orderOfCheckout.map { it.name }}      ")
        }
    }

    producers.joinAll()
    channel.close()
    consumer.join()
    println("Final order: ${orderOfCheckout.map { it.name }}")
}

/**
 * Extension function to simulate checkout processing for a shopper,
 * then send the shopper into the channel.
 *
 * @param shopper The shopper to send.
 *
 * Simulates background work by delaying before sending.
 */
private suspend fun Channel<Shopper>.checkoutShopper(shopper: Shopper) {
    delay(10)                  // simulate background work
    this.send(shopper)         // send result from child
}

