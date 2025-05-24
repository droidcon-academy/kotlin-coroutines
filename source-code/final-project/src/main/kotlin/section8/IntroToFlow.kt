package org.example.section8

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * Demonstrates how to collect items from a Flow using coroutines.
 *
 * This example:
 * - Creates a flow of shoppers.
 * - flow collects each emitted shopper and logs their name.
 * - orderOfCheckout tracks the order in which shoppers are collected.
 * - Prints orderOfCheckout.
 */
fun main(): Unit = runBlocking {

    // flow does not activate - flow is cold
    val flow = shoppers()
    val orderOfCheckout = mutableListOf<Shopper>()

    // flow "turns" on - flow is hot
    flow.collect { shopper ->
        log("shopper collected: ${shopper.name}")
        orderOfCheckout.add(shopper)
    }

    log("Final order: ${orderOfCheckout.map { it.name }}")
}

/**
 * Creates a flow that emits a list of Shopper objects sequentially. The flow
 * completes normally and triggers the onCompletion callback.
 *
 * @return A Flow emitting shoppers in order.
 */
fun shoppers(): Flow<Shopper> = flow {
    listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("Ren", 3),
    ).forEach {
        log("  shopper emitted: ${it.name}")
        emit(it)
    }
}.flowOn(Dispatchers.Default)
    .onCompletion {
        log("Shopper flow completed")
    }

/**
 * Creates a Flow that emits a list of Shopper objects one by one.
 *
 * The flow completes normally and triggers the `onCompletion` callback.
 *
 * @return A Flow emitting shoppers in order.
 */
fun shoppersError(): Flow<Shopper> = flow {
    listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        throw Exception("flow failed"),
        Shopper("Ren", 3),
    ).forEach {
        log("shopper collected: ${it.name}")
        emit(it)
    }
}.catch { e ->
    log("Error caught: ${e.message}")
}.onCompletion {
    log("Shopper flow completed")
}