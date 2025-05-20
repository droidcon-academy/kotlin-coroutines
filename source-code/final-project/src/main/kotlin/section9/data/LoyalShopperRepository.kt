package org.example.section9.data

import org.example.section9.Result

interface LoyalShopperRepository {
    /**
     * Verify the user is part of the shopper loyalty program by checking db
     */
    suspend fun verifyShopper(id: String): Result<Boolean>
}