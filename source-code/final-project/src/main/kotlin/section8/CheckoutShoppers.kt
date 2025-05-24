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

class CheckoutShopper() {
    private val _state = MutableStateFlow<CheckoutState>(CheckoutState.NotStarted)
    val state: StateFlow<CheckoutState> get() = _state.asStateFlow()


    fun checkoutShoppers(list: List<Shopper>) {
        _state.tryEmit(CheckoutState.InProgress)

        list.forEach { shopper ->
            // checkout background work here
            _state.tryEmit(CheckoutState.CheckoutSuccess(shopper))
        }
    }
}