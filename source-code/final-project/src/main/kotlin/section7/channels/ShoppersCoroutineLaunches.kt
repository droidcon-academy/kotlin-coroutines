package org.example.section7.channels

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

data class Shopper(val name: String, val groceryCartItems: Int)

fun main(): Unit = runBlocking {

    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("REE", 3),
    )
    val channel = Channel<Shopper>()			           // <-- 1. add here
    val orderOfCheckout = mutableListOf<Shopper>()


    shoppers.forEach { shopper ->
        launch(Dispatchers.Default) {
            channel.checkoutShopper(shopper)               // <-- 2. send elements from channel
            log("curr order in child launch: ${orderOfCheckout.map { it.name }}")
        }
    }

    for (i in channel) {
        orderOfCheckout.add(i)                               // <-- 3. receive elements here
        log("Received in parent: ${i.name}")
        log("Current checkout order: ${orderOfCheckout.map { it.name }}")
    }

    channel.close()
    println("Final order: ${orderOfCheckout.map { it.name }}")
}


// Channel extension function
private suspend fun Channel<Shopper>.checkoutShopper(shopper: Shopper) {
    delay(10)                  // simulate background work
    this.send(shopper)         // send result from child
}

