package org.example.section8.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.example.log
import org.example.section8.CoroutineContextProvider
import org.example.section8.CoroutineContextProviderImpl
import org.example.section8.domain.model.CheckoutState
import org.example.section8.domain.model.Shopper

/**
 *
 */
class Register(
    val scope: CoroutineScope,
    val checkoutShopper: Checkout, //CheckoutShopper,
    val dispatcher: CoroutineContextProvider = CoroutineContextProviderImpl(),
) {

    init {
        scope.launch(dispatcher.defaultDispatcher) {
            checkoutShopper.state.collect { state ->
                when (state) {
                    is CheckoutState.NotStarted -> {
                        log("State: CheckoutState.NotStarted - Register is not active.")
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
                                    "Cause of error: ${state.error.message}."
                        )
                    }
                }
            }
        }
    }

    fun startCheckout(shoppers: List<Shopper>) {
        scope.launch(dispatcher.defaultDispatcher) {
            val flow = flow()

            flow.collect { shopper ->
                log("shopper starting checkout: ${shopper.name}")
                checkoutShopper.checkoutShopper(shopper)
            }
        }
    }

    fun flow(): Flow<Shopper> = flow {
        listOf(
            Shopper("id", "Jake", 3),
            Shopper("id", "Zubin", 5),
            Shopper("id", "Amber", 4),
            Shopper("id", "REE", 3),
        ).forEach {
            emit(it)
        }
    }
}
