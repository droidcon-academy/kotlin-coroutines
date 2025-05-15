package org.example.section8.domain.model

sealed class CheckoutState {
    data object NotStarted : CheckoutState()
    data object InProgress : CheckoutState()
    data class CheckoutSuccess(val checkout: Shopper) : CheckoutState()
    data class CheckoutSuccessLoyal(val checkout: Shopper) : CheckoutState()
    data class CheckoutError(val error: Error) : CheckoutState()
}