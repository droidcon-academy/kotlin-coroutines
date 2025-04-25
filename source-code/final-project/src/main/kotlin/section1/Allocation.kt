package org.example.section1

import org.example.log
import org.example.section1.Shopper
import java.lang.Thread.sleep

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