package org.example.section8.domain

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.section8.CoroutineContextProvider
import org.example.section8.CoroutineContextProviderImpl
import org.example.section8.Result
import org.example.section8.data.LoyalShopperRepository
import org.example.section8.domain.model.CheckoutState
import org.example.section8.domain.model.Shopper

val ceh = CoroutineExceptionHandler { _, e ->
    println("CEH caught unhandled exception $e")
}

interface Checkout {
    val state: StateFlow<CheckoutState>

    fun checkoutShopper(shopper: Shopper)
}

class CheckoutShopper(
    val dispatcher: CoroutineContextProvider = CoroutineContextProviderImpl(),
    val loyalShopperRepository: LoyalShopperRepository,
): Checkout {

    private val _state = MutableStateFlow<CheckoutState>(CheckoutState.NotStarted)
    override val state: StateFlow<CheckoutState> get() = _state.asStateFlow()

    val scope: CoroutineScope = CoroutineScope(dispatcher.defaultDispatcher)

    override fun checkoutShopper(shopper: Shopper) {
        _state.tryEmit(CheckoutState.InProgress)

        scope.launch(ceh) {
            if (verifyShopperLoyalty(shopper.shopperId)) {
                _state.tryEmit(CheckoutState.CheckoutSuccessLoyal(shopper))
            } else {
                _state.tryEmit(CheckoutState.CheckoutSuccess(shopper))
            }
        }
    }

    suspend fun verifyShopperLoyalty(id: String): Boolean {
        return when (val result = loyalShopperRepository.verifyShopper(id)) {
            is Result.Success -> {
                result.data
            }
            is Result.Failure -> {
                // would be better to log the exception and return false to prevent
                // the app from breaking, but for now we
                println("Unable to verify shopper discount")
                return false
            }
        }
    }
}