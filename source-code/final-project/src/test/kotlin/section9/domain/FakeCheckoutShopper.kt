package section9.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.section9.domain.Checkout
import org.example.section9.domain.model.Shopper
import org.example.section9.domain.model.CheckoutState

class FakeCheckoutShopper(
    private val loyalMemberIds: List<String>,
    private val error: Exception? = null,
) : Checkout {

    val checkoutHistory = mutableListOf<Shopper>()

    private val _state = MutableStateFlow<CheckoutState>(CheckoutState.NotStarted)
    override val state: StateFlow<CheckoutState>
        get() = _state.asStateFlow()

    override fun checkoutShopper(shopper: Shopper) {
        error?.let {
            _state.tryEmit(CheckoutState.CheckoutError(Error(it)))
        } ?: {
            checkoutHistory.add(shopper)
            if (loyalMemberIds.contains(shopper.shopperId)) {
                _state.tryEmit(CheckoutState.CheckoutSuccessLoyal(shopper))
            } else {
                _state.tryEmit(CheckoutState.CheckoutSuccess(shopper))
            }
        }
    }
}