package org.example.section1

import org.example.log
import java.lang.Thread.sleep
import java.util.concurrent.Executors

val checkoutHistory = HashMap<String, Int>()

data class Shopper(var name: String, var items: Int)

fun main() {
    val checkoutLane = Executors.newFixedThreadPool(3)
    val shopper1 = CheckoutShopper("Jake", 1)
    val shopper2 = Shopper("Zubin", 10)
    val shopper3 = CheckoutShopper("Amber", 4)
    val shopper4 = CheckoutShopper("Jake", 2)

    checkoutLane.apply {
        try {
            val thread = CheckoutLane(shopper2)
            val future = submit { thread.start() }
            thread.interrupt()
            future.get()
        } catch (e: Error) {
            println("Thread interrupted: $e")
        } finally {
            shutdown()
        }
    }
}


class CheckoutLane(val shopper: Shopper) : Thread() {
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

class CheckoutShopper(
    val name: String,
    val numberOfItems: Int,
) : Runnable {

    private var running = true

    override fun run() {
        while (running) {
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

    public fun stopThread() {
        running = false
    }
}