package org.example.section7.channels

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log
import org.example.section7.Shopper
import java.util.Collections

fun main(): Unit = runBlocking {

    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("Ren", 3),
    )
    val channel = Channel<Shopper>()			           // <-- 1. add here
    val orderOfCheckout = Collections.synchronizedList<Shopper>(mutableListOf<Shopper>())

    val producers = launch(Dispatchers.Default) {
        shoppers.map { shopper ->
            channel.checkoutShopper(shopper)               // <-- 2. send elements from channel
            log("curr order in child launch: ${shopper.name}       | snapshot: ${orderOfCheckout.map { it.name }}")
        }
    }.join()

    val consumer = launch(Dispatchers.Default) {
        for (i in channel) {
            orderOfCheckout.add(i)
            log("curr order in parent: ${orderOfCheckout.map { it.name }}      ")
        }
    }

    channel.close()
    consumer.join()
    println("Final order: ${orderOfCheckout.map { it.name }}")
}


// Channel extension function
private suspend fun Channel<Shopper>.checkoutShopper(shopper: Shopper) {
    delay(10)                  // simulate background work
    this.send(shopper)         // send result from child
}

