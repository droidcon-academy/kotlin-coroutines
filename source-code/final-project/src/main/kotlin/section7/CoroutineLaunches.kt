import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log
import org.example.section1.Shopper

fun main(): Unit = runBlocking {
    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 4),
        Shopper("Amber", 4),
        Shopper("Ren", 3),
    )
    val orderOfCheckout = mutableListOf<Shopper>()

    val producers = shoppers.map { shopper ->
        launch(Dispatchers.Default) {
            log("sending: ${shopper.name}")
            orderOfCheckout.checkoutShopper(shopper, orderOfCheckout)
        }
    }.joinAll()

    println("final order: ${orderOfCheckout.map { it.name }}")
}

private suspend fun MutableList<Shopper>.checkoutShopper(shopper: Shopper, orderOfCheckout: List<Shopper>) {
    delay(10)	// simulate background work
    println("received: ${shopper.name}")
    this.add(shopper)
    log("curr checkout snapshot: ${orderOfCheckout.map { it.name }}")
}
