package section8

import org.example.section8.data.LoyalShopperRepository
import org.example.section8.Result

class FakeLoyalShopperRepository(
    val verifyShopperResult: Result<Boolean>,
) : LoyalShopperRepository {

    override suspend fun verifyShopper(id: String): Result<Boolean> {
        return verifyShopperResult
    }
}