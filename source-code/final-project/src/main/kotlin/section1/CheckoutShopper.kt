package org.example.section1

import org.example.log

/**
 * A Runnable tasks that checks out a shopper
 *
 * For every `CheckoutShopper`, a new thread is spawned to process a $numberOfItems.
 * During the checkout, a shopper is added to the checkout history and each item is logged
 * to simulate scanning.
 *
 * @param name - name of shopper
 * @param numberOfItems - number of items in a shopper's cart
 */
internal class CheckoutShopper(
    val name: String,
    val numberOfItems: Int,
) : Runnable {

    /**
     * Runs work as a thread
     */
    override fun run() {
        println("    $name has $numberOfItems items. Checking out...")
        (1..numberOfItems).forEachIndexed { i, elem ->
            log("item $elem scanned for $name.")
        }
        println("    $name is checked out!")
    }
}
