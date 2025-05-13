package org.example.section7.flows

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking
import org.example.log

fun main(): Unit = runBlocking {

    val flow = shoppers()
    val orderOfCheckout = mutableListOf<Shopper>()


    flow.collect { shopper ->
        log("shopper collected: ${shopper.name}")
        orderOfCheckout.add(shopper)
    }

    println("Final order: ${orderOfCheckout.map { it.name }}")
}

fun shoppers(): Flow<Shopper> = flow {
    listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("REE", 3),
    ).forEach {
        emit(it)
    }
}.onCompletion {
    println("I have completed")
}