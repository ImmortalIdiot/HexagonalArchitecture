package domain.model

import java.time.LocalDate

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val expiredAt: LocalDate
) {
    override fun toString(): String {
        return "$name ($price руб.)"
    }
} 