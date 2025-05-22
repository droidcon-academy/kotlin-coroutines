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

val ceh = CoroutineExceptionHandler { _, e ->
    println("CEH caught unhandled exception $e")
}

interface Checkout {
    val state: StateFlow<CheckoutState>
    fun checkoutShopper(shopper: Shopper)
}

internal class CheckoutShopper(
    private val loyalShopperRepository: LoyalShopperRepository,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher.defaultDispatcher),
    private val dispatcher: CoroutineContextProvider = CoroutineContextProviderImpl(),
): Checkout {

    private val _state = MutableStateFlow<CheckoutState>(CheckoutState.NotStarted)
    override val state: StateFlow<CheckoutState> get() = _state.asStateFlow()

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