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
    val channel = Channel<Shopper>()
    val orderOfCheckout = mutableListOf<Shopper>()


    shoppers.forEach { shopper ->
        launch(Dispatchers.Default) {
            channel.checkoutShopper(shopper)
            log("curr order in child launch: ${orderOfCheckout.map { it.name }}")
        }
    }

    repeat(shoppers.size + 1) {
        orderOfCheckout.add(channel.receive())
        log("curr order in parent      : ${orderOfCheckout.map { it.name }}")
    }


    println("Final order: ${orderOfCheckout.map { it.name }}")
}


// Make extension function for Channel
suspend fun Channel<Shopper>.checkoutShopper(shopper: Shopper) {
    delay(10) // simulate background work
    this.send(shopper)               // send result from child
}

/*suspend fun MutableList<Shopper>.checkoutShopper(shopper: Shopper) {
    delay(10) // simulate background work
    this.add(shopper)
}*/

