package section8.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.example.section8.Result
import org.example.section8.domain.Checkout
import org.example.section8.domain.CheckoutShopper
import org.example.section8.domain.Register
import org.example.section8.domain.models.CheckoutState
import org.example.section8.domain.models.Shopper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import section8.FakeLoyalShopperRepository
import section8.TestDispatcherProvider

class RegisterTest {

    val fakeCheckout = object : Checkout {
        override val state: StateFlow<CheckoutState>
            get() = TODO("Not yet implemented")

        override fun checkoutShopper(shopper: Shopper) {
            TODO("Not yet implemented")
        }

    }

    val register = Register(
        scope = CoroutineScope(Job()),
        checkoutShopper = fakeCheckout,
        dispatcher = TestDispatcherProvider(),
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewModel is init state is initialized`() = runTest {
        val loyalShopperRepository = FakeLoyalShopperRepository(
            verifyShopperResult = Result.Success(true),
        )

        val sut = CheckoutShopper(
            dispatcher = TestDispatcherProvider(),
            loyalShopperRepository = loyalShopperRepository,
        )

        Assertions.assertEquals(sut.state.value, CheckoutState.NotStarted)
    }

    @Test
    fun `checkout shopper flow`() = runTest {
        //flow().
    }
}