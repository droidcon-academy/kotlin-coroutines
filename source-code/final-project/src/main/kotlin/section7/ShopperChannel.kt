package org.example.section7

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * Entry point of the program that simulates concurrent shopper checkout using coroutines and a Channel.
 */
fun main(): Unit = runBlocking {

    // Define a list of shoppers waiting to check out
    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("Ren", 3),
    )

    // Create a channel to communicate between producer (shopper) and consumer (checkout)
    val channel = Channel<Shopper>()                    // <-- 1. channel created for sending shoppers to checkout
    val orderOfCheckout = mutableListOf<Shopper>()

    // Launch a coroutine for each shopper to send themselves to the channel
    val producers = shoppers.map { shopper ->
        launch(Dispatchers.Default) {
            log("sending: ${shopper.name}")
            channel.checkoutShopper(shopper)            // <-- 2. send each shopper to the channel
        }
    }

    // Collect shoppers in the order they are processed
    val consumer = launch(Dispatchers.Default) {
        repeat(shoppers.size) {
            val shopper = channel.receive()			        // <-- 3. receive elements here
            orderOfCheckout.add(shopper)
            println("received: ${shopper.name}")
            log("curr checkout snapshot: ${orderOfCheckout.map { it.name }}")
        }
    }

    producers.joinAll()     // Wait for all producer coroutines to complete
    channel.close()         // Close the channel to signal no more items will be sent
    consumer.join()         // Wait for the consumer coroutine to finish
    println("final order: ${orderOfCheckout.map { it.name }}")
}


// Channel extension function
private suspend fun Channel<Shopper>.checkoutShopper(shopper: Shopper) {
    delay(10)                  // simulate background work
    this.send(shopper)         // send result from child
}

