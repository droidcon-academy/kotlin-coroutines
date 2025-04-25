package org.example.section7

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

fun main(): Unit = runBlocking {

    val channel = Channel<Int>()                  // <-- add channel
    val order = mutableListOf<Int>()


    (1..5).forEach { num ->
        launch(Dispatchers.Default) {
             channel.checkoutShopper(num)         // <-- add channel
            log("curr order in child launch: ${order.map { it }}")
        }
    }

    repeat(5) {
         order.add(channel.receive())        // <-- receive in parent
        log("curr order in parent      : ${order.map { it }}")
    }


    println("Final order: ${order.map { it }}")
}


// Make extension function for Channel
internal suspend fun Channel<Int>.checkoutShopper(num: Int) {
    delay(10)                  // simulate background work
    this.send(num)             // send result from child
}

suspend fun MutableList<Int>.checkoutShopper(num: Int) {
    delay(10)                  // simulate background work
    this.add(num)
}

