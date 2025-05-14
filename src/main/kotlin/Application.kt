import adapter.primary.ConsoleManager
import adapter.secondary.notification.ConsoleNotificationService
import domain.port.primary.OrderService
import domain.port.secondary.NotificationRepository
import domain.service.OrderServiceImpl
import domain.port.secondary.OrderRepository
import domain.port.secondary.ProductRepository
import adapter.secondary.persistance.OrderRepositoryImpl
import adapter.secondary.persistance.ProductRepositoryImpl

fun main() {
    val orderRepositoryImpl: OrderRepository = OrderRepositoryImpl()
    val productRepositoryImpl: ProductRepository = ProductRepositoryImpl()
    val notificationRepository: NotificationRepository = ConsoleNotificationService()

    val orderManagementService: OrderService = OrderServiceImpl(
        orderRepositoryImpl,
        productRepositoryImpl,
        notificationRepository
    )

    val consoleUI = ConsoleManager(orderManagementService)
    consoleUI.start()
} 