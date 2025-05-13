package adapter.secondary.notification

import domain.model.Order
import domain.port.secondary.NotificationRepository

class ConsoleNotificationService : NotificationRepository {
    override fun notifySupplier(orderId: String) {
        println("\n[УВЕДОМЛЕНИЕ] Поставщик принял заказ №$orderId")
    }

    override fun notifyOrderStatusChanged(order: Order) {
        println("\n[УВЕДОМЛЕНИЕ] Статус заказа #${order.id} изменен на: ${order.status}")
    }
} 