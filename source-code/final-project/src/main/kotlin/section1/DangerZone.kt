package org.example.section1

import org.example.log
import java.lang.Thread.sleep

data class Shopper(var name: String, var items: Int)

/**
 * Danger Zone!
 *
 * checkout1 and checkout2 threads access and modify the same shopper
 * concurrently without protection of state or synchronization. This
 * can lead to race conditions and inconsistent state.
 *
 */
fun main() {
    val shopper = Shopper("Jake", 1)

    val checkout1 = Thread {
        val localRef = shopper
        sleep(3)
        localRef.items = 3
        log("checkout1 for ${localRef.name}: ${localRef.items}")
    }

    val checkout2 = Thread {
        val localRef = shopper
        log("checkout2 for ${localRef.name}: ${localRef.items}")
    }

    checkout1.start()
    checkout2.start()
}