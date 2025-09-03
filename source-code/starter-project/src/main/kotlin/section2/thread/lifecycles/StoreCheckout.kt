package section2.thread.lifecycles

import java.lang.Thread.sleep
import java.util.concurrent.Executors

fun main() {
    val checkoutLane = Executors.newFixedThreadPool(1)
    val shopper = CheckoutShopper(Shopper("Zubin", 10))

    checkoutLane.apply {
        try {
            val future = submit {
                shopper.run()
                sleep(3000)				// <-- add interruption
            }
            shopper.stopThread()			// <-- call stopThread
            future.get()
        } catch(e: Error) {
            println(e)
        } finally {
            shutdown()
        }
    }
}
