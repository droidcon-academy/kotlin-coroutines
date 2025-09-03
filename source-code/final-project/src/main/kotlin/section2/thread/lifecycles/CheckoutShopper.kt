package org.example.section2.thread.lifecycles

import org.example.log
import java.io.IO.println
import java.util.HashMap

/**
 * Intentionally **not thread-safe** structure for demonstration purposes.
 *
 * When accessed by multiple threads concurrently, the map gets race conditions,
 * creating inconsistent or corrupted state.
 */
internal val checkoutHistory = HashMap<String, Int>()

/**
 * A thread describing a shopper getting checked out by extending the Runnable class.
 *
 * Demonstrates how to get a thread to cooperatively cancel by adding a volatile boolean flag.
 *
 * @param shopper - shopper processed for checkout
 */
internal class CheckoutShopper(val shopper: Shopper) : Thread() {

    override fun run() {
        try {
            println("    ${shopper.name} has ${shopper.items} items. Checking out...")

            repeat(shopper.items) { item ->
                val oldValue = checkoutHistory[name] ?: 0
                checkoutHistory.put(name, oldValue + 1)
                log("item $item scanned for ${shopper.name}.  | Checkout history check: ${checkoutHistory}")
            }
            println("    ${shopper.name} is checked out!")
        } catch(e: InterruptedException) {
            println("CheckoutLane Thread interrupted! $e")
        }
    }
}