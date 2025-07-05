import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log
import org.example.section1.Shopper

/**
 * Main entry point of the program.
 *
 * Simulates multiple shoppers concurrently checking out.
 * Each shopper is processed in a separate coroutine launched on the Default dispatcher.
 * The order in which shoppers complete checkout is recorded in [orderOfCheckout].
 */
fun main(): Unit = runBlocking {
    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 4),
        Shopper("Amber", 4),
        Shopper("Ren", 3),
    )
    // Mutable list to hold shoppers in the order they finish checkout
    val orderOfCheckout = mutableListOf<Shopper>()

    // Launch a coroutine for each shopper to simulate checkout processing concurrently
    val producers = shoppers.map { shopper ->
        launch(Dispatchers.Default) {
            log("sending: ${shopper.name}")
            orderOfCheckout.checkoutShopper(shopper, orderOfCheckout)
        }
    }.joinAll()

    println("final order: ${orderOfCheckout.map { it.name }}")
}

/**
 * Extension suspend function that simulates the checkout process for a shopper.
 *
 * @param shopper The shopper currently being processed.
 * @param orderOfCheckout The list maintaining the current order of checked-out shoppers.
 *
 * This function simulates some background work via [delay], then adds the shopper
 * to the checkout list and logs the current snapshot.
 */
private suspend fun MutableList<Shopper>.checkoutShopper(shopper: Shopper, orderOfCheckout: List<Shopper>) {
    delay(10)	// simulate background work
    println("received: ${shopper.name}")
    this.add(shopper)
    log("curr checkout snapshot: ${orderOfCheckout.map { it.name }}")
}
