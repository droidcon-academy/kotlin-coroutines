package org.example.section7.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

fun main(): Unit = runBlocking {

    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("REE", 3),
    )
    val checkoutLane1: ReceiveChannel<Shopper> = checkoutLane(shoppers)
    val orderOfCheckout = mutableListOf<Shopper>()

    for (shopper in checkoutLane1) {
        orderOfCheckout.add(shopper)
        log("curr order in parent      : ${orderOfCheckout.map { it.name }}")
    }

    println("Final order: ${orderOfCheckout.map { it.name }}")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.checkoutLane(shoppers: List<Shopper>) = produce {
    shoppers.forEach { shopper ->
        delay(10)
        send(shopper)
    }
}


