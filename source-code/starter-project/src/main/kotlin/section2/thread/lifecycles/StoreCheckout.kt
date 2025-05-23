package section2.thread.lifecycles

import section2.CheckoutLane
import section2.Shopper
import java.util.concurrent.Executors

fun main() {
    val checkoutLane = Executors.newFixedThreadPool(3)
    val shopper = Shopper("Zubin", 10)

    checkoutLane.apply {
        try {
            val thread = CheckoutLane(shopper)
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