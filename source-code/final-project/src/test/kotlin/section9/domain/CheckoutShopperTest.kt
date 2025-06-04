package section9.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.example.section9.Result
import org.example.section9.domain.CheckoutShopper
import org.example.section9.domain.model.CheckoutState
import org.example.section9.domain.model.Shopper
import org.junit.jupiter.api.Test
import section9.data.FakeLoyalShopperRepository
import section9.TestDispatcherProvider
import section9.UnconfinedTestDispatcherProvider
import kotlin.test.assertEquals

/**
 * Trying out the Coroutine Testing Framework. This test demonstrates use of:
 * - StandardTestDispatcher
 * - UnconfinedTestDispatcher
 */
internal class CheckoutShopperTest {

    private val shopper = Shopper("id", "Jake", 3)

    /**
     * Tests for initial state
     * - Simple tests ensures protection against accidental regression.
     */
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
        assertEquals(CheckoutState.NotStarted, sut.state.value)
    }

    /**
     * StandardTestDispatcher - for testing execution order
     *  - good for tests
     *  - cooperates with runTest and scheduler
     *  - stable for async code
     *
     *  With a StandardTestDispatcher, coroutine do not execute until you
     *  have called the testScheduler to advance the coroutines.
     */
    @ExperimentalCoroutinesApi
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
        advanceUntilIdle()              // advances all coroutines

        assertEquals(CheckoutState.CheckoutSuccessLoyal(shopper), sut.state.value)
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

        assertEquals(CheckoutState.CheckoutSuccessLoyal(shopper), sut.state.value)
    }
}