package section2.thread.lifecycles

import log

/**
 * Intentionally **not thread-safe** structure for demonstration purposes.
 *
 * When accessed by multiple threads concurrently, the map gets race conditions,
 * creating inconsistent or corrupted state.
 */
internal val checkoutHistory = HashMap<String, Int>()

/**
 * A task describing a shopper getting checked out by extending the Runnable class.
 *
 * Demonstrates how to get a thread to cooperatively cancel by adding a volatile boolean flag.
 *
 * @param shopper - shopper getting checked out in line
 */
internal class CheckoutShopper(val shopper: Shopper) : Thread() {

    override fun run() {
        println("    $name has ${shopper.items} items. Checking out...")
        repeat(shopper.items) { item ->
            // TODO
        }
        println("    $name is checked out!")
    }
}

