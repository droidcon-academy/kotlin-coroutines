package org.example.section2.thread.lifecycles

import java.util.concurrent.Executors

fun main() {
    val checkoutLane = Executors.newFixedThreadPool(3)
    val shopper = Shopper("Zubin", 10)

    checkoutLane.apply {
        try {
            val checkout = CheckoutLane(shopper)
            val future = submit { checkout.start() }
            checkout.interrupt()
            future.get()
        } catch (e: Error) {
            println("Thread interrupted: $e")
        } finally {
            shutdown()
        }
    }
}