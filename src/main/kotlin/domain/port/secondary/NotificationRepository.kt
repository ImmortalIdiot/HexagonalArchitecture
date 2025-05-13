package domain.port.secondary

import domain.model.Order

interface NotificationRepository {
    fun notifyOrderStatusChanged(order: Order)
    
    fun notifySupplier(orderId: String)
} 
