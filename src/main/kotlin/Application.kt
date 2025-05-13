import adapter.primary.ConsoleManager
import adapter.secondary.notification.ConsoleNotificationService
import adapter.secondary.persistance.InMemoryOrderRepository
import adapter.secondary.persistance.InMemoryProductRepository
import domain.port.primary.OrderService
import domain.port.secondary.NotificationRepository
import domain.port.secondary.OrderRepository
import domain.port.secondary.ProductRepository
import domain.service.OrderServiceImpl

fun main() {
    val orderRepository: OrderRepository = InMemoryOrderRepository()
    val productRepository: ProductRepository = InMemoryProductRepository()
    val notificationRepository: NotificationRepository = ConsoleNotificationService()

    val orderManagementService: OrderService = OrderServiceImpl(
        orderRepository,
        productRepository,
        notificationRepository
    )

    val consoleUI = ConsoleManager(orderManagementService)

    println("Запуск системы управления заказами.")
    consoleUI.start()
} 