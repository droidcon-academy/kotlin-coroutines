import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    shoppers.forEach { shopper ->
        launch(Dispatchers.Default) {
            orderOfCheckout.checkoutShopper(shopper)
            log("curr order in child launch: ${shopper.name} order in child launch: ${orderOfCheckout.map { it.name }}")
        }
        repeat(shoppers.size) {
            log("curr order in parent		: ${orderOfCheckout.map { it.name }}")
        }

        println("Final order: ${orderOfCheckout.map { it.name }}")
    }
}

data class Shopper(val name: String, val groceryCartItems: Int)

private suspend fun MutableList<Shopper>.checkoutShopper(shopper: Shopper) {
    delay(10)	// simulate background work
    this.add(shopper)
}
