package adapter.secondary.persistance

import domain.model.Order
import domain.port.secondary.OrderRepository

class OrderRepositoryImpl : OrderRepository {
    private val orders = HashMap<String, Order>()

    override fun save(order: Order) {
        orders[order.id] = order
    }

    override fun findById(id: String): Order {
        return orders[id]!!
    }

    override fun findAll(): List<Order> {
        return ArrayList(orders.values)
    }

    override fun deleteOrder(orderId: String) {
        orders.remove(orderId)
    }
} 