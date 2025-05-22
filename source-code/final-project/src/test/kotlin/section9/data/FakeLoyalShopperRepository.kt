package section9.data

import org.example.section9.Result
import org.example.section9.data.LoyalShopperRepository

/**
 * Fake LoyalShopeprRepository implementation for testing.
 *
 * @param verifyShopperResult is the result to "mock" for calling verifyShopper.
 */
class FakeLoyalShopperRepository(
    val verifyShopperResult: Result<Boolean>,
) : LoyalShopperRepository {

    override suspend fun verifyShopper(id: String): Result<Boolean> {
        return verifyShopperResult
    }
}