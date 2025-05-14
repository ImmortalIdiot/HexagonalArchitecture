import adapter.primary.ConsoleManager
import adapter.secondary.notification.ConsoleNotificationService
import domain.port.primary.OrderService
import domain.port.secondary.NotificationRepository
import domain.service.OrderServiceImpl

fun main() {
    val orderRepository: domain.port.secondary.OrderRepository = adapter.secondary.persistance.OrderRepository()
    val productRepository: domain.port.secondary.ProductRepository = adapter.secondary.persistance.ProductRepository()
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