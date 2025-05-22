package section9.data

import org.example.section9.Result
import org.example.section9.data.LoyalShopperRepository

class FakeLoyalShopperRepository(
    val verifyShopperResult: Result<Boolean>,
) : LoyalShopperRepository {

    override suspend fun verifyShopper(id: String): Result<Boolean> {
        return verifyShopperResult
    }
}