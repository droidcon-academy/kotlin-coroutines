package section2.thread.lifecycles

import log
import java.lang.Thread.sleep

/**
 * not thread-safe* data structure.
 */
internal val checkoutHistory = HashMap<String, Int>()

/**
 * A task describing a shopper getting checked out by extending the Runnable class.
 *
 * Demonstrates how to get a thread to cooperatively cancel by adding a volatile boolean flag.
 *
 * @param name - name of shopper
 * @param numberOfItems - the amount of items in the shopper's cart for scanning
 */
class CheckoutShopper(
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
        println("    $name is checked out!")
    }
}