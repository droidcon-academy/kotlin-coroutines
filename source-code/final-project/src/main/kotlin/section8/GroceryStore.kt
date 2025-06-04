package org.example.section8

import kotlinx.coroutines.runBlocking

/**
 * Entry point to simulate the checkout process using a [Register].
 *
 * This function runs in a blocking coroutine context to allow suspending functions to be called
 * without needing a full coroutine setup (ideal for small demos or test runs).
 */
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
