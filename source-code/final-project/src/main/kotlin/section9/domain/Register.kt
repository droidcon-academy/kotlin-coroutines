package org.example.section9.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.example.log
import org.example.section9.CoroutineContextProvider
import org.example.section9.CoroutineContextProviderImpl
import org.example.section9.domain.model.CheckoutState
import org.example.section9.domain.model.Shopper

/**
 * Handles the registration and checkout process for a list of [Shopper]s.
 *
 * Listens to checkout state updates and logs relevant information based on current state.
 *
 * @property checkoutShopper An instance of [Checkout] used to initiate shopper checkouts.
 * @property dispatcher Provides coroutine dispatchers to allow for testability and separation of concerns.
 */
class Register(
    internal val checkoutShopper: Checkout,
    private val dispatcher: CoroutineContextProvider = CoroutineContextProviderImpl(),
) {

    // Coroutine scope tied to this Register instance, using a SupervisorJob to isolate child failures.
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        scope.launch(dispatcher.defaultDispatcher) {
            checkoutShopper.state.collect { state ->
                // Collect state changes from checkoutShopper and react accordingly.
                when (state) {
                    is CheckoutState.NotStarted -> {
                        log("State: NotStarted - Register is not active.")
                    }

                    is CheckoutState.InProgress -> {
                        log("State: CheckoutState.InProgress - Register is starting checkout.")
                    }

                    is CheckoutState.CheckoutSuccessLoyal -> {
                        log(
                            "State: CheckoutState.CheckoutSuccess - Checkout success " +
                                    "for loyal customer ${state.processed.name}."
                        )
                    }

                    is CheckoutState.CheckoutSuccess -> {
                        log(
                            "State: CheckoutState.CheckoutSuccess - Checkout success " +
                                    "for ${state.processed.name}."
                        )
                    }

                    is CheckoutState.CheckoutError -> {
                        log(
                            "State: CheckoutState.CheckoutError - Checkout failed. " +
                                    "Cause of error: ${state.message}."
                        )
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
        val flow = flow(shoppers)

        scope.launch(dispatcher.defaultDispatcher) {
            flow.collect { shopper ->
                log("shopper starting checkout: ${shopper.name}")
                checkoutShopper.checkoutShopper(shopper)
            }
        }
    }

    /**
     * Emits a [Flow] of [Shopper] objects from a given list.
     *
     * @param shoppers The list of shoppers to emit.
     * @return A [Flow] that emits each shopper one by one.
     */
    fun flow(shoppers: List<Shopper>): Flow<Shopper> = flow {
        shoppers.forEach { emit(it) }
    }
}
