package org.example.section8

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

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