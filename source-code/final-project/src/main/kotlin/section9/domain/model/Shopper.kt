package org.example.section9.domain.model

/**
 * Represents a shopper going through the checkout process.
 *
 * @property shopperId Unique identifier for the shopper.
 * @property name The shopper's display name.
 * @property groceryCartItems The number of items currently in the shopper's cart.
 */
data class Shopper(
    val shopperId: String,
    val name: String,
    val groceryCartItems: Int,
)