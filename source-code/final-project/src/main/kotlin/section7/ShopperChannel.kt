package org.example.section7

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

fun main(): Unit = runBlocking {

    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("Ren", 3),
    )
    val channel = Channel<Shopper>()                       // <-- 1. add here
    val orderOfCheckout = mutableListOf<Shopper>()

    val producers = shoppers.map { shopper ->
        launch(Dispatchers.Default) {
            log("sending: ${shopper.name}")
            channel.checkoutShopper(shopper)               // <-- 2. send elements from channel
        }
    }

    // Collect shoppers in the order they are processed
    val consumer = launch(Dispatchers.Default) {
        repeat(shoppers.size) {
            val shopper = channel.receive()			// <-- 3. receive elements here
            orderOfCheckout.add(shopper)
            println("received: ${shopper.name}")
            log("curr checkout snapshot: ${orderOfCheckout.map { it.name }}")
        }
    }

    producers.joinAll()
    channel.close()
    consumer.join()
    println("final order: ${orderOfCheckout.map { it.name }}")
}


// Channel extension function
private suspend fun Channel<Shopper>.checkoutShopper(shopper: Shopper) {
    delay(10)                  // simulate background work
    this.send(shopper)         // send result from child
}

