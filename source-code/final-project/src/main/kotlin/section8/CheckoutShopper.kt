package org.example.section8

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.forEach

/**
 * Represents a shopper in the grocery checkout system.
 *
 * @property name Name of the shopper.
 * @property groceryCartItems Number of items the shopper has in their cart.
 */
data class Shopper(
    val name: String,
    val groceryCartItems: Int,
)

/**
 * Represents the various states the checkout process can be in.
 */
sealed class CheckoutState {
    data object NotStarted : CheckoutState()
    data object InProgress : CheckoutState()
    data class CheckoutSuccess(val shopper: Shopper) : CheckoutState()
    data class CheckoutError(val error: Error) : CheckoutState()
}

/**
 * Manages the checkout process and exposes its state as a reactive flow.
 */
class CheckoutShopper() {
    private val _state = MutableSharedFlow<CheckoutState>()
    val state: SharedFlow<CheckoutState> get() = _state.asSharedFlow()

    /**
     * Begins the checkout process for a list of shoppers.
     * Emits [InProgress] at the start, then [CheckoutSuccess] for each shopper.
     *
     * In a real system, this would include async work such as payment, validation, etc.
     *
     * @param list List of shoppers to be processed for checkout.
     */
    fun checkoutShoppers(list: List<Shopper>) {
        _state.tryEmit(CheckoutState.InProgress)

        list.forEach { shopper ->
            // checkout background work here
            _state.tryEmit(CheckoutState.CheckoutSuccess(shopper))
        }
    }
}