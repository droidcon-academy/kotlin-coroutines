package org.example.section9.domain

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.section9.CoroutineContextProvider
import org.example.section9.CoroutineContextProviderImpl
import org.example.section9.Result
import org.example.section9.data.LoyalShopperRepository
import org.example.section9.domain.model.CheckoutState
import org.example.section9.domain.model.Shopper

/**
 * A CoroutineExceptionHandler to catch and log unhandled exceptions in coroutines.
 */
val ceh = CoroutineExceptionHandler { _, e ->
    println("CEH caught unhandled exception $e")
}

/**
 * Defines the contract for a checkout process.
 */
interface Checkout {
    // A flow representing the current state of the checkout process.
    val state: StateFlow<CheckoutState>

    /**
     * Initiates the checkout process for a given [Shopper].
     *
     * @param shopper The shopper to process.
     */
    fun checkoutShopper(shopper: Shopper)
}

/**
 * Implementation of the [Checkout] interface that manages the checkout logic.
 *
 * @param loyalShopperRepository Repository used to verify shopper loyalty.
 * @param scope CoroutineScope to manage structured concurrency.
 * @param dispatcher Abstraction for coroutine context dispatchers, useful for testing.
 */
internal class CheckoutShopper(
    private val loyalShopperRepository: LoyalShopperRepository,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher.defaultDispatcher),
    private val dispatcher: CoroutineContextProvider = CoroutineContextProviderImpl(),
): Checkout {

    private val _state = MutableStateFlow<CheckoutState>(CheckoutState.NotStarted)
    override val state: StateFlow<CheckoutState> get() = _state.asStateFlow()

    /**
     * Starts the checkout process for a single [shopper].
     *
     * Emits [CheckoutState.InProgress], and then either [CheckoutSuccessLoyal] or [CheckoutSuccess],
     * depending on loyalty status. If an exception is thrown, it is caught by [ceh].
     */
    override fun checkoutShopper(shopper: Shopper) {
        _state.tryEmit(CheckoutState.InProgress)

        scope.launch(ceh) {
            if (verifyShopperLoyalty(shopper.shopperId)) {
                println("${shopper.name} is part of the loyalty program")
                _state.tryEmit(CheckoutState.CheckoutSuccessLoyal(shopper))
            } else {
                println("${shopper.name} is not part of the loyalty program")
                _state.tryEmit(CheckoutState.CheckoutSuccess(shopper))
            }
        }
    }


    /**
     * Verifies whether a shopper is part of the loyalty program by consulting the repository.
     *
     * @param id The shopper ID to verify.
     * @return `true` if the shopper is loyal, `false` otherwise or if verification fails.
     */
    private suspend fun verifyShopperLoyalty(id: String): Boolean {
        return when (val result = loyalShopperRepository.verifyShopper(id)) {
            is Result.Success -> {
                result.data
            }
            is Result.Failure -> {
                println("Unable to verify shopper discount")
                false
            }
        }
    }
}