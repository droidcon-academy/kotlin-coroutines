package org.example.section8

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.log

/**
 * Handles the registration and checkout process for a list of [Shopper]s.
 *
 * Listens to checkout state updates and logs relevant information based on current state.
 *
 * @property checkoutShopper An instance of [Checkout] used to initiate shopper checkouts.
 */
class Register(
    val checkoutShopper: CheckoutShopper = CheckoutShopper(),
) {

    // Coroutine scope tied to this Register instance, using a SupervisorJob to isolate child failures.
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        scope.launch(Dispatchers.Default) {
            checkoutShopper.state.collect { state ->
                // Collect state changes from checkoutShopper and react accordingly.
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

    /**
     * Starts the checkout process for a list of [Shopper]s.
     *
     * @param shoppers List of shoppers to process through checkout.
     */
    fun startCheckout(shoppers: List<Shopper>) {
        scope.launch(Dispatchers.Default) {
            delay(100)
            checkoutShopper.checkoutShoppers(shoppers)
        }
    }
}