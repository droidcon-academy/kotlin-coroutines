package org.example.section7.flows

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log
import kotlin.collections.forEach

data class Shopper(
    val name: String,
    val groceryCartItems: Int,
)

fun main(): Unit = runBlocking {

    val shoppers = listOf(
        Shopper("Jake", 3),
        Shopper("Zubin", 5),
        Shopper("Amber", 4),
        Shopper("REE", 3)
    )

    val register = Register(this)
    register.startCheckout(shoppers)
}

sealed class CheckoutState {
    data object NotStarted : CheckoutState()
    data object InProgress : CheckoutState()
    data class CheckoutSuccess(val shopper: Shopper) : CheckoutState()
    data class CheckoutError(val error: Error) : CheckoutState()
}


class Register(
    val scope: CoroutineScope,
    val checkoutShopper: CheckoutShopper = CheckoutShopper(),
) {

    init {
        scope.launch(Dispatchers.Default) {
            checkoutShopper.state.collect { state ->
                when (state) {
                    is CheckoutState.NotStarted -> {
                        log("State: CheckoutState.NotStarted - Register is not active.")
                    }
                    is CheckoutState.InProgress -> {
                        log("State: CheckoutState.InProgress - Register is starting checkout.")
                    }
                    is CheckoutState.CheckoutError -> {
                        log("State: CheckoutState.CheckoutError - Checkout failed. " +
                                "Cause of error: ${state.error.message}.")
                    }
                    is CheckoutState.CheckoutSuccess ->  {
                        log("State: CheckoutState.CheckoutSuccess - Checkout success for ${state.shopper.name}.")
                    }
                }
            }
        }
    }


    fun startCheckout(shoppers: List<Shopper>) {
        scope.launch(Dispatchers.Default) {
            delay(100)
            checkoutShopper.checkoutShoppers(shoppers)
        }
    }
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