package org.example.section7

/**
 * Represents a shopper in the grocery checkout system.
 *
 * @property name Name of the shopper.
 * @property groceryCartItems Number of items the shopper has in their cart.
 */
internal data class Shopper(val name: String, val groceryCartItems: Int)