package org.example.section8

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.log

class Register(
    val checkoutShopper: CheckoutShopper = CheckoutShopper(),
) {

    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        scope.launch(Dispatchers.Default) {
            checkoutShopper.state.collect { state ->
                when (state) {
                    is CheckoutState.NotStarted -> {
                        log("CheckoutState: NotStarted - Register is not active.")
                    }
                    is CheckoutState.InProgress -> {
                        log("CheckoutState: InProgress - Register is starting checkout.")
                    }
                    is CheckoutState.CheckoutError -> {
                        log("CheckoutState: CheckoutError - Checkout failed. " +
                                "Cause of error: ${state.error.message}.")
                    }
                    is CheckoutState.CheckoutSuccess ->  {
                        log("CheckoutState: CheckoutSuccess - Checkout success " +
                                "for ${state.shopper.name}.")
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