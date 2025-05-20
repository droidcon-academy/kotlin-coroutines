package section9

import org.example.section9.data.LoyalShopperRepository
import org.example.section9.Result

class FakeLoyalShopperRepository(
    val verifyShopperResult: Result<Boolean>,
) : LoyalShopperRepository {

    override suspend fun verifyShopper(id: String): Result<Boolean> {
        return verifyShopperResult
    }
}