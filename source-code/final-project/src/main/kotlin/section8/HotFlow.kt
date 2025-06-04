package org.example.section8

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import org.example.log

/**
 * Entry point for demonstrating a hot [StateFlow] that emits [Shopper] objects.
 *
 * The flow is converted to a [StateFlow] using [stateIn] with [SharingStarted.Eagerly],
 * which means it begins emitting values immediately, regardless of active collectors.
 */
fun main(): Unit = runBlocking {

    val orderOfCheckout = mutableListOf<Shopper>()

    // flow is hot and running
    val statefulShoppers: StateFlow<Shopper?> = shoppers()
        .stateIn(
            scope = this,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    statefulShoppers.collect { shopper ->
        log("shopper collected: ${shopper?.name}")
        shopper?.let { shopper }
    }

    log("Final order: ${orderOfCheckout.map { it.name }}")
}