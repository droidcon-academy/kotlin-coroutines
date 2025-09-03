package org.example.section2.thread.lifecycles

import java.lang.Thread.sleep
import java.util.concurrent.Executors

// Main function demonstrating manual thread management with interruptions
fun main() {
    // Simulate checkout lanes
    val checkoutLane = Executors.newFixedThreadPool(3)
    val shopper = Shopper("Zubin", 10)

    checkoutLane.apply {
        try {
            val checkout = CheckoutShopper(shopper)

            // Submit the checkout task to the executor
            val future = submit {
                checkout.start()    // Start the checkout thread
                sleep(300)
            }

            // Attempt to interrupt the thread after submission
            checkout.interrupt()

            // Wait for the task to complete
            future.get()
        } catch (e: Error) {
            println("Thread interrupted: $e")
        } finally {
            // Shutdown the executor to release resources
            shutdown()
        }
    }
}