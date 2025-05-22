package org.example.section2

import org.example.log
import org.example.section9.domain.model.Shopper
import java.lang.Thread.sleep

class checkoutLane(val shopper: Shopper) : Thread() {
    override fun run() {
        println("    $name has ${shopper.items} items. Checking out...")

        sleep(1000)
        repeat(shopper.items) { item ->
            val oldValue = checkoutHistory[name] ?: 0
            checkoutHistory.put(name, oldValue + 1)
            log("item $item scanned for $name.  | Checkout history check: ${checkoutHistory}")
        }
        println("    $name is checked out!")
    }

}

fun main() {
    val shopper2 = CheckoutShopper("Zubin", 10)

    val run = shopper2.run()
    sleep(2000)
    //run.
}