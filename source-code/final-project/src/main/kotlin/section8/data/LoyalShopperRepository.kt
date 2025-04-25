package org.example.section8.data

import org.example.section8.Result

interface LoyalShopperRepository {
    /**
     * Verify the user is part of the shopper loyalty program by checking db
     */
    suspend fun verifyShopper(id: String): Result<Boolean>
}