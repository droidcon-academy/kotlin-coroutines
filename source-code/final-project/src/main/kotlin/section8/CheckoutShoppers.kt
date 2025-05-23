package org.example.section8

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import kotlin.collections.forEach

data class Shopper(
    val name: String,
    val groceryCartItems: Int,
)

sealed class CheckoutState {
    data object NotStarted : CheckoutState()
    data object InProgress : CheckoutState()
    data class CheckoutSuccess(val shopper: Shopper) : CheckoutState()
    data class CheckoutError(val error: Error) : CheckoutState()
}

fun main(): Unit = runBlocking {

    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("Ren", 3)
    )

    val register = Register(this)
    register.startCheckout(shoppers)
}

class CheckoutShopper() {
    private val _state = MutableStateFlow<CheckoutState>(CheckoutState.NotStarted)
    val state: StateFlow<CheckoutState> get() = _state.asStateFlow()


    fun checkoutShoppers(list: List<Shopper>) {
        _state.tryEmit(CheckoutState.InProgress)

        list.forEach { shopper ->
            _state.tryEmit(CheckoutState.CheckoutSuccess(shopper))
        }
    }
}