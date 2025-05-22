package section9.domain

import app.cash.turbine.test
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.example.section9.domain.model.CheckoutState
import org.example.section9.domain.Register
import org.example.section9.domain.model.Shopper
import org.junit.jupiter.api.Assertions
import section9.TestDispatcherProvider
import kotlin.test.Test

/**
 * Testing hot + cold flows with Turbine
 */
internal class RegisterTest {

    private val shoppers = listOf(
        Shopper("id1", "Jake", 3),
        Shopper("id2", "Zubin", 5),
        Shopper("id3", "Amber", 4),
        Shopper("id4", "Ren", 3),
    )

    /**
     * Testing initial state.
     */
    @Test
    fun `When register is initialized state then returns NotStarted`() = runTest {
        // given
        val results = listOf("id1", "id3")
        val fakeCheckout = FakeCheckoutShopper(results)

        // when
        val sut = Register(
            checkoutShopper = fakeCheckout,
            dispatcher = TestDispatcherProvider(testScheduler),
        )

        // then
        Assertions.assertEquals(CheckoutState.NotStarted, fakeCheckout.state.value)
        Assertions.assertEquals(fakeCheckout.checkoutHistory, emptyList<Shopper>())
    }

    /**
     * Testing for cold flow with Turbine. Must wait for each item with awaitItem().
     */
    @Test
    fun `Cold flow emits all shoppers in order`() = runTest {
        // given
        val results = listOf("id1", "id3")
        val fakeCheckout = FakeCheckoutShopper(results)
        val sut = Register(
            checkoutShopper = fakeCheckout,
            dispatcher = TestDispatcherProvider(testScheduler),
        )

        // when
        sut.flow(shoppers).test {

            // then
            Assertions.assertEquals(shoppers[0], awaitItem())
            Assertions.assertEquals(shoppers[1], awaitItem())
            Assertions.assertEquals(shoppers[2], awaitItem())
            Assertions.assertEquals(shoppers[3], awaitItem())
            awaitComplete()     // complete the flow
        }
    }

    /**
     * Test StateFlow with Turbine
     */
    @Test
    fun `When register checks out shoppers then returns last value and records all shoppers`() = runTest {
        // given
        val shopper = listOf(shoppers[0])
        val results = listOf("id1", "id4")
        val fakeCheckout = FakeCheckoutShopper(results)
        val sut = Register(
            checkoutShopper = fakeCheckout,
            dispatcher = TestDispatcherProvider(testScheduler),
        )

        fakeCheckout.state.test {
            skipItems(1) // skips NotStarted initial state

            // when
            sut.startCheckout(shopper)
            advanceUntilIdle()

            // then
            Assertions.assertEquals(CheckoutState.CheckoutSuccessLoyal(shopper.first()), awaitItem())
            Assertions.assertEquals(fakeCheckout.checkoutHistory, shopper)
        }
    }
}