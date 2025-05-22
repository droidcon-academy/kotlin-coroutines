package section9.domain

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import kotlinx.coroutines.test.runTest
import org.example.section9.domain.model.CheckoutState
import org.example.section9.domain.Register
import org.example.section9.domain.model.Shopper
import org.junit.jupiter.api.Assertions
import section9.TestDispatcherProvider
import kotlin.test.Test
import kotlin.test.assertEquals

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
        val fakeCheckout = FakeCheckoutShopper(emptyList())

        // when
        val sut = Register(
            checkoutShopper = fakeCheckout,
            dispatcher = TestDispatcherProvider(testScheduler),
        )

        // then
        assertEquals(CheckoutState.NotStarted, fakeCheckout.state.value)
        assertEquals(fakeCheckout.checkoutHistory, emptyList<Shopper>())
    }

    /**
     * Testing for cold flow emission order with Turbine. Must wait for each item with awaitItem().
     *
     * sut.flow(shoppers).test { ... } launches a coroutine and calls collect on the flow.
     *      - must ensure all events have been consumed + completed
     *      - a hanging test fails the test
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
        val test = sut.flow(shoppers).test {
            // then
            assertEquals(shoppers[0], awaitItem())
            assertEquals(shoppers[1], awaitItem())
            assertEquals(shoppers[2], awaitItem())
            assertEquals(shoppers[3], awaitItem())
            awaitComplete()     // complete the flow
            cancel()
            ensureAllEventsConsumed()
        }
    }

    /**
     * Test StateFlow with Turbine - Verifies that state emission is correct.
     *
     * Turbine Notes:
     *  - sut.checkoutShopper.state.test { ... } launches a coroutine and calls collect on the flow.
     *  - must ensure all events have been consumed + completed
     *  - a hanging test fails the test
     */
    @Test
    fun `Test verify checkout state emission - HotFlow  `() = runTest {
        // given
        val results = listOf("id1", "id4")
        val fakeCheckout = FakeCheckoutShopper(results)
        val sut = Register(
            checkoutShopper = fakeCheckout,
            dispatcher = TestDispatcherProvider(testScheduler),
        )

        // when
        val turbine = sut.checkoutShopper.state.test {        // test guarantee that the flow under test runs up to first suspension
            skipItems(1)                        // skips NotStarted initial state
            sut.startCheckout(shoppers)

            // then
            assertEquals(CheckoutState.CheckoutSuccessLoyal(shoppers[0]), awaitItem())
            assertEquals(CheckoutState.CheckoutSuccess(shoppers[1]), awaitItem())
            assertEquals(CheckoutState.CheckoutSuccess(shoppers[2]), awaitItem())
            assertEquals(CheckoutState.CheckoutSuccessLoyal(shoppers[3]), awaitItem())
            assertEquals(shoppers, fakeCheckout.checkoutHistory)

            cancel()
            ensureAllEventsConsumed()
        }
    }

    /**
     * Tests shopper loyalty error handling during checkout.
     *
     * Ensures that exceptions are caught and wrapped in a Result instead of
     * throw the error.
     *
     * FakeCheckoutShopper is configured to throw an exception, which will not throw an error
     * thanks to the wrapping Result class.
     *
     * Turbine notes:
     *  - Turbine is a Channel that can collect flows for validation
     *  - Calling cancel() cleans up backed coroutines in Turbine
     *  - This test observes that the collecting CheckoutState in Register
     *     receives the right emissions.
     */
    @Test
    fun `Test shopper loyalty error handling - StateFlow`() = runTest {
        // given
        val exception = Exception("I have failed!")
        val results = listOf("id1", "id4")
        val fakeCheckout = FakeCheckoutShopper(results, exception)
        val sut = Register(
            checkoutShopper = fakeCheckout,
            dispatcher = TestDispatcherProvider(testScheduler),
        )

        // when
        val turbine = sut.checkoutShopper.state.test {        // test guarantee that the flow under test runs up to first suspension
            skipItems(1)                        // skips NotStarted initial state
            sut.startCheckout(shoppers)

            // then
            assertEquals(CheckoutState.CheckoutError("I have failed!"), awaitItem())
            assertEquals(0, fakeCheckout.checkoutHistory.size)

            cancel()
            ensureAllEventsConsumed()
        }
    }

    /**
     * Testing multiple flows with Turbine. To work with multiple flows, wrap with turbineScope { ... }
     *
     * Turbine allows you to save values with .testIn(backgroundScope), and then you can interact with
     * each of the individual flows.
     *
     * This test shows how to observe:
     * - A cold flow, emitting shoppers one-by-one
     * - A hot StateFlow, which emits updates on checkout state.
     *
     * With testIn, ensureAllEventsConsumed() is called on completion, so it is important that you cancel/complete
     * and consume all flows/events.
     */
    @Test
    fun `Test multiple flows - cold flow and StateFlow`() = runTest {
        // given
        val results = listOf("id1", "id4")
        val fakeCheckout = FakeCheckoutShopper(results)
        val sut = Register(
            checkoutShopper = fakeCheckout,
            dispatcher = TestDispatcherProvider(testScheduler),
        )

        // when
        val turbine = turbineScope {
            val coldFlow = sut.flow(listOf(shoppers[0])).testIn(backgroundScope)
            val hotFlow = sut.checkoutShopper.state.testIn(backgroundScope)
            sut.startCheckout(shoppers)
            hotFlow.skipItems(1)

            assertEquals(shoppers[0], coldFlow.awaitItem())
            assertEquals(CheckoutState.CheckoutSuccessLoyal(shoppers[0]), hotFlow.awaitItem())

            // cleanup
            hotFlow.cancelAndConsumeRemainingEvents()  // handles and consumes remaining events
            coldFlow.awaitComplete()
        }
    }
}