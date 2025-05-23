package org.example.section2

import org.example.log
import org.example.section2.thread.lifecycles.checkoutHistory

data class Shopper(val name: String, val items: Int)

internal class CheckoutLane(val shopper: Shopper) : Thread() {
    override fun run() {
        try {
            println("    $name has ${shopper.items} items. Checking out...")

            repeat(shopper.items) { item ->
                val oldValue = checkoutHistory[name] ?: 0
                checkoutHistory.put(name, oldValue + 1)
                log("item $item scanned for $name.  | Checkout history check: ${checkoutHistory}")
            }
            println("    $name is checked out!")
        } catch(e: InterruptedException) {
            println("CheckoutLane Thread interrupted! $e")
        }
    }
}