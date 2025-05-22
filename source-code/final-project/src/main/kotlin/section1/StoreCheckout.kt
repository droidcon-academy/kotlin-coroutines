package org.example.section1

import java.util.concurrent.Executors

internal val checkoutHistory = HashMap<String, Int>()

data class Shopper(var name: String, var items: Int)

fun main() {
    val checkoutLane = Executors.newFixedThreadPool(3)
    val shopper1 = CheckoutShopper("Jake", 1)
    val shopper2 = Shopper("Zubin", 10)
    val shopper3 = CheckoutShopper("Amber", 4)
    val shopper4 = CheckoutShopper("Jake", 2)

    checkoutLane.apply {
        try {
            val thread = CheckoutLane(shopper2)
            val future = submit { thread.start() }
            thread.interrupt()
            future.get()
        } catch (e: Error) {
            println("Thread interrupted: $e")
        } finally {
            shutdown()
        }
    }
}