package domain.port.secondary

import domain.model.Order

interface OrderRepository {
    fun save(order: Order)
    
    fun findById(id: String): Order
    
    fun findAll(): List<Order>
    
    fun deleteOrder(orderId: String)
} 