package org.example.section2.thread.lifecycles

import org.example.log
import kotlin.text.get

/**
 * You can stop a thread cooperatively  by adding a volatile boolean flag.
 */
class CheckoutShopper(
    val name: String,
    val numberOfItems: Int,
) : Runnable {

    private var running = true

    override fun run() {
        while (running) {
            println("    $name has $numberOfItems items. Checking out...")

            Thread.sleep(1000)
            repeat(numberOfItems) { item ->
                val oldValue = checkoutHistory[name] ?: 0
                checkoutHistory.put(name, oldValue + 1)
                log("item $item scanned for $name.  | Checkout history check: ${checkoutHistory}")
            }
            println("    $name is checked out!")
        }
    }

    public fun stopThread() {
        running = false
    }
}