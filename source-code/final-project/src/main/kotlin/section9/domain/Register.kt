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
 *
 */
class Register(
    private val checkoutShopper: CheckoutShopper,
    private val dispatcher: CoroutineContextProvider = CoroutineContextProviderImpl(),
) {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        /*scope.launch(dispatcher.defaultDispatcher) {
            checkoutShopper.state.collect { state ->
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
                                    "Cause of error: ${state.error.message}."
                        )
                    }
                }*/
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
