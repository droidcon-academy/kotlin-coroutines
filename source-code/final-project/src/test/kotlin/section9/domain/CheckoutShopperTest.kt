package section9.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.example.section9.Result
import org.example.section9.domain.CheckoutShopper
import org.example.section9.domain.model.CheckoutState
import org.example.section9.domain.model.Shopper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import section9.data.FakeLoyalShopperRepository
import section9.TestDispatcherProvider
import section9.UnconfinedTestDispatcherProvider

internal class CheckoutShopperTest {

    private val shopper = Shopper("id", "Jake", 3)

    @Test
    fun `viewModel initializes and returns NotStarted state`() = runTest {
        // given
        val loyalShopperRepository = FakeLoyalShopperRepository(
            verifyShopperResult = Result.Success(true),
        )

        // when
        val sut = CheckoutShopper(
            loyalShopperRepository = loyalShopperRepository,
            dispatcher = TestDispatcherProvider(testScheduler),
        )

        // then
        Assertions.assertEquals(CheckoutState.NotStarted, sut.state.value)
    }

    /**
     * TestDispatcher - for testing execution order
     *  - good for tests
     *  - cooperates with runTest and scheduler
     *  - stable for async code
     */
    @Test
    fun `viewModel checks out shopper successfully - testing control flow`() = runTest {
        // given
        val loyalShopperRepository = FakeLoyalShopperRepository(
            verifyShopperResult = Result.Success(true),
        )
        val sut = CheckoutShopper(
            loyalShopperRepository = loyalShopperRepository,
            dispatcher = TestDispatcherProvider(testScheduler),
        )

        // when
        sut.checkoutShopper(shopper)
        advanceUntilIdle()

        Assertions.assertEquals(CheckoutState.CheckoutSuccessLoyal(shopper), sut.state.value)
    }

    /**
     * Unconfined - for testing control flow
     *  - good for tests
     *  - cooperates with runTest and scheduler
     *  - sometimes flaky for async code
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewModel checks out shopper successfully unconfined - eager execution`() = runTest {
        // given
        val loyalShopperRepository = FakeLoyalShopperRepository(
            verifyShopperResult = Result.Success(true),
        )
        val sut = CheckoutShopper(
            loyalShopperRepository = loyalShopperRepository,
            dispatcher = UnconfinedTestDispatcherProvider(testScheduler),
        )

        // when
        sut.checkoutShopper(shopper)

        Assertions.assertEquals(CheckoutState.CheckoutSuccessLoyal(shopper), sut.state.value)
    }
}