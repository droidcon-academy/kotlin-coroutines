package org.example.section7.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.example.log
import org.example.section7.Shopper

fun main(): Unit = runBlocking {

    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("Ren", 3),
    )
    val checkoutLane: ReceiveChannel<Shopper> = checkoutLane(shoppers) // does it not need to close?
    val orderOfCheckout = mutableListOf<Shopper>()

    for (shopper in checkoutLane) {
        orderOfCheckout.add(shopper)
        log("curr order in parent      : ${orderOfCheckout.map { it.name }}")
    }

    println("final order: ${orderOfCheckout.map { it.name }}")
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun CoroutineScope.checkoutLane(shoppers: List<Shopper>) = produce {
    shoppers.forEach { shopper ->
        delay(10)
        send(shopper)
    }
}


