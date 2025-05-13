package domain.service

import domain.model.Order
import domain.model.OrderItem
import domain.model.OrderStatus
import domain.model.Product
import domain.port.primary.OrderService
import domain.port.secondary.NotificationRepository
import domain.port.secondary.OrderRepository
import domain.port.secondary.ProductRepository

class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val notificationRepository: NotificationRepository
) : OrderService {

    override fun createOrder(products: List<OrderItem>): String {
        val newOrder = Order(products.toMutableList())
        orderRepository.save(newOrder)
        return newOrder.id
    }

    override fun addProductToOrder(orderId: String, productId: String, quantity: Int) {
        val order = getOrderOrThrow(orderId)
        val product = getProductOrThrow(productId)

        order.addItem(product, quantity)
        orderRepository.save(order)
    }

    override fun removeProductFromOrder(orderId: String, itemIndex: Int) {
        val order = getOrderOrThrow(orderId)

        order.removeItem(itemIndex)
        orderRepository.save(order)
    }

    override fun changeOrderStatus(orderId: String, newStatus: OrderStatus) {
        val order = getOrderOrThrow(orderId)

        order.changeStatus(newStatus)
        orderRepository.save(order)

        if (newStatus == OrderStatus.CONFIRMED) {
            notificationRepository.notifySupplier(orderId)
        }
        notificationRepository.notifyOrderStatusChanged(order)
    }

    override fun getOrderItems(id: String): List<OrderItem> {
        return orderRepository.findById(id).products
    }

    override fun getAllOrders(): List<Order> {
        return orderRepository.findAll()
    }

    override fun getOrderById(id: String): Order {
        return orderRepository.findById(id)
    }

    override fun checkProductsDate(id: String) {
        orderRepository.findById(id).checkProducts()
    }

    override fun findAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    override fun deleteOrder(orderId: String) {
        orderRepository.deleteOrder(orderId)
    }

    private fun getOrderOrThrow(orderId: String): Order {
        val order = orderRepository.findById(orderId)

        if (order == null) {
            throw IllegalArgumentException("Заказ не найден: $orderId")
        }

        return order
    }

    private fun getProductOrThrow(productId: String): Product {
        val product = productRepository.findById(productId)

        if (product == null) {
            throw IllegalArgumentException("Товар не найден: $productId")
        }

        return product
    }
} 