package domain.model

import java.time.LocalDate

data class OrderItem(
    val product: Product,
    val quantity: Int
) {
    val totalPrice: Double
        get() = product.price * quantity

    val expiredDate: LocalDate
        get() = product.expiredAt

    override fun toString(): String {
        return "${product.name} x $quantity = $totalPrice руб."
    }
} 