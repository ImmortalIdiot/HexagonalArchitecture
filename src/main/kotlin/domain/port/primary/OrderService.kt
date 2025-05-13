package domain.port.primary

import domain.model.Order
import domain.model.OrderItem
import domain.model.OrderStatus
import domain.model.Product

interface OrderService {
    fun createOrder(products: List<OrderItem>): String
    
    fun addProductToOrder(orderId: String, productId: String, quantity: Int)
    
    fun removeProductFromOrder(orderId: String, itemIndex: Int)
    
    fun changeOrderStatus(orderId: String, newStatus: OrderStatus)
    
    fun getOrderItems(id: String): List<OrderItem>
    
    fun getAllOrders(): List<Order>
    
    fun getOrderById(id: String): Order
    
    fun checkProductsDate(id: String)
    
    fun findAllProducts(): List<Product>
    
    fun deleteOrder(orderId: String)
} 