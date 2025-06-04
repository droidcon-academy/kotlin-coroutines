package org.example.section9.domain.model

/**
 * Represents the various states of the checkout process in the application.
 *
 * This sealed class allows exhaustive `when` statements, ensuring all states are handled.
 */
sealed class CheckoutState {
    data object NotStarted : CheckoutState()
    data object InProgress : CheckoutState()
    data class CheckoutSuccess(val processed: Shopper) : CheckoutState()
    data class CheckoutSuccessLoyal(val processed: Shopper) : CheckoutState()
    data class CheckoutError(val message: String) : CheckoutState()
}