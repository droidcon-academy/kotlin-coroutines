package org.example.section8

import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {

    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("Ren", 3)
    )

    val register = Register()
    register.startCheckout(shoppers)
}
