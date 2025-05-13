package section7.flows

data class Shopper(
    val name: String,
    val groceryCartItems: Int,
)

sealed class CheckoutState {
    data object NotStarted : CheckoutState()
    data object InProgress : CheckoutState()
    data class CheckoutSuccess(val shopper: Shopper) : CheckoutState()
    data class CheckoutError(val error: Error) : CheckoutState()
}