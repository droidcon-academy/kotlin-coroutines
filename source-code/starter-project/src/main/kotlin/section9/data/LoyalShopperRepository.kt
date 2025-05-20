package section9.data

interface LoyalShopperRepository {
    /**
     * Verify the user is part of the shopper loyalty program by checking db
     */
    suspend fun verifyShopper(id: String): Result<Boolean>
}