package org.example.section1

import java.util.concurrent.Executors

/**
 * Simulates shopper checkout for 3 shoppers in a single
 */
fun main() {
    val checkoutLane = Executors.newFixedThreadPool(2)

    val shopper1 = CheckoutShopper("Jake", 1)
    val shopper2 = CheckoutShopper("Zubin", 10)
    val shopper3 = CheckoutShopper("Amber", 4)

    checkoutLane.apply {
        try {
            submit { shopper1.run() }.get()
            submit { shopper2.run() }.get()
            submit { shopper3.run() }.get()
        } catch (e: Error) {
            println("Thread interrupted: $e")
        } finally {
            shutdown()
        }
    }
}
