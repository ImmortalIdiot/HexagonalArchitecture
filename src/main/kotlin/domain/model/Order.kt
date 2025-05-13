package domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class Order(
    var products: MutableList<OrderItem>
) {
    val id: String = UUID.randomUUID().toString()
    var status: OrderStatus = OrderStatus.CREATED
    private var cost: Double = calculateTotalPrice()
    private val createdAt: LocalDateTime = LocalDateTime.now()

    fun addItem(product: Product, quantity: Int) {
        if (quantity <= 0) {
            throw IllegalArgumentException("Количество товара должно быть положительным числом")
        }
        products.add(OrderItem(product, quantity))
    }

    fun removeItem(index: Int) {
        if (index < 0 || index >= products.size) {
            throw IllegalArgumentException("Некорректный индекс товара")
        }
        products.removeAt(index)
    }

    fun changeStatus(newStatus: OrderStatus) {
        if (!isValidStatusTransition(this.status, newStatus)) {
            throw IllegalStateException("Недопустимый переход статуса: $status -> $newStatus")
        }
        this.status = newStatus
    }

    fun checkProducts() {
        if (products.isEmpty()) {
            throw IllegalArgumentException("В заказе нет товаров.")
        }

        val today = LocalDate.now()
        var counter = 0

        for (i in products.indices) {
            val item = products[i]
            val expiry = item.expiredDate

            if (expiry.isBefore(today)) {
                counter++
                println("Просрочен продукт [№${i + 1}]: ${item.product.name}")
            }
        }

        if (counter == 0) {
            println("Просрочки не обнаружено.")
        }
    }

    private fun isValidStatusTransition(current: OrderStatus, next: OrderStatus): Boolean {
        return when (current) {
            OrderStatus.CREATED -> next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELED
            OrderStatus.CONFIRMED -> next == OrderStatus.GOING || next == OrderStatus.CANCELED
            OrderStatus.GOING -> next == OrderStatus.DELIVERED || next == OrderStatus.CANCELED
            OrderStatus.DELIVERED -> next == OrderStatus.CANCELED
            else -> false
        }
    }

    fun calculateTotalPrice(): Double {
        val price = products.sumOf { it.totalPrice }
        this.cost = price
        return price
    }

    override fun toString(): String {
        return "Order{\n id='$id', \n cost=$cost, \n status=$status, \n createdAt=$createdAt, \n products=$products \n}"
    }
}