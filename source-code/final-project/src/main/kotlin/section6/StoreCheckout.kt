package org.example.section6

import org.example.log
import java.lang.Thread.sleep
import java.util.concurrent.Executors

/**
 * Demonstrates how exceptions in threads and thread pools behave, highlighting
 * the difficulties in handling exceptions in multi-threaded environments.
 *
 * This example uses a fixed thread pool to run shopper checkout tasks. Shows:
 * - The use of a default uncaught exception handler for all threads.
 * - How exceptions inside submitted Runnable tasks are caught or lost.
 * - Nested threads created inside a Runnable and how exceptions there are handled.
 */
fun main() {

    // Set a global uncaught exception handler for all threads in the JVM
    Thread.setDefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler { thread: Thread?, throwable: Throwable? ->
        System.err.println("Uncaught exception in thread: " + thread!!.getName())
        throwable?.printStackTrace()
    })

    val checkoutLane = Executors.newFixedThreadPool(3)
    val shopper1 = CheckoutShopper("Jake", 3)
    val shopper2 = CheckoutShopper("Zubin", 10)
    val shopper3 = CheckoutShopper("Amber", 4)
    val shopper4 = CheckoutShopper("Ren", 3)


    checkoutLane.apply {
        try {
            submit { shopper1.run() }
            submit { shopper2.run() }
            submit { shopper3.run() }
            submit { shopper4.run() }
        } catch (e: Exception) {
            log("Exception in Executors task submission: $e")
        } finally {
            println("Executor thread pool shut down.")
            shutdown()
        }
    }
}

/**
 * Represents a shopper performing checkout in a thread pool. Implements Runnable so it
 * can be submitted to an Executor.
 */
private class CheckoutShopper(
    val name: String,
    val numberOfItems: Int,
) : Runnable {

    /**
     * Runs the checkout task.
     * Demonstrates exception handling inside a Runnable submitted to Executor.
     *
     * Note: This method catches exceptions, but some exceptions thrown
     * inside nested threads might be uncaught.
     */
    override fun run() {
        try {
            oneShoppingLane(name, numberOfItems)
            throw Exception("mistakes")
            log("shopper thread continues")
            checkoutShopper(name, numberOfItems)
        } catch(e: Exception) {
            log("Exception caught: ${e.message}")
        }
    }
}

/**
 * Starts a nested thread that performs checkout. Demonstrates how
 * exceptions in nested threads are handled separately.
 */
private fun oneShoppingLane(
    name: String,
    numberOfItems: Int,
) {
    Thread {
        try {
            if (Thread.interrupted()) return@Thread
            checkoutShopper(name, numberOfItems)
            Exception("not another one!")
        } catch (e: Exception) {
            log("Exception caught in child thread: ${e.message}")
        }
    }.start()
}

/**
 * Simulates the checkout process for a shopper. Prints progress and sleeps
 * proportional to the number of items.
 */
fun checkoutShopper(name: String, numberOfItems: Int) {
    log("Checking out $name.    ")
    sleep(1000L * numberOfItems)
    println("    $name has $numberOfItems items. Checking out...")
    (1..numberOfItems).forEachIndexed { i, elem ->
        println("        item $elem scanned for $name.")
    }
    println("    $name is checked out!")
}