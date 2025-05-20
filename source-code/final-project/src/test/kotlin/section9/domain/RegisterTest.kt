package section9.domain

/*class RegisterTest {

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
        dispatcher = section9.TestDispatcherProvider(),
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
            dispatcher = section9.TestDispatcherProvider(),
            loyalShopperRepository = loyalShopperRepository,
        )

        Assertions.assertEquals(sut.state.value, CheckoutState.NotStarted)
    }

    @Test
    fun `checkout shopper flow`() = runTest {
        //flow().
    }
}*/