package org.example.section1

import org.example.log
import java.lang.Thread.sleep

internal class CheckoutShopper(
    val name: String,
    val numberOfItems: Int,
) : Runnable {

    override fun run() {
        println("    $name has $numberOfItems items. Checking out...")

        sleep(1000)
        repeat(numberOfItems) { item ->
            val oldValue = checkoutHistory[name] ?: 0
            checkoutHistory.put(name, oldValue + 1)
            log("item $item scanned for $name.  | Checkout history check: ${checkoutHistory}")
        }
    }
}