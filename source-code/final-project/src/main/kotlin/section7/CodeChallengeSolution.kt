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
 * Main function demonstrating concurrent producer-consumer pattern with synchronization.
 *
 * The program:
 * - Creates a list of shoppers.
 * - Uses a [Channel] to send shoppers from producer coroutines to a consumer coroutine.
 * - Uses a [Mutex] to ensure mutual exclusion around sending shoppers to the channel.
 * - Uses a thread-safe synchronized list to track the order of checkout.
 */
fun main(): Unit = runBlocking {

    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("Ren", 3),
    )
    val mutex = Mutex()                                    // <-- 1. add here
    val channel = Channel<Shopper>()
    val orderOfCheckout = Collections.synchronizedList<Shopper>(mutableListOf<Shopper>())

    val consumer = launch(Dispatchers.Default) {
        for (i in channel) {
            orderOfCheckout.add(i)
            log("curr order in parent: ${orderOfCheckout.map { it.name }}      ")
        }
    }

    val producers = shoppers.map { shopper ->
        launch(Dispatchers.Default) {
           mutex.withLock {                               // <-- 2. add mutex lock
                log("curr order in child launch: ${shopper.name}")
                channel.checkoutShopper(shopper)
           }
        }
    }

    producers.joinAll()
    channel.close()
    consumer.join()
    println("Final order: ${orderOfCheckout.map { it.name }}")
}


// Channel extension function
private suspend fun Channel<Shopper>.checkoutShopper(shopper: Shopper) {
    delay(10)                  // simulate background work
    this.send(shopper)         // send result from child
}

