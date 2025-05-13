package domain.model

enum class OrderStatus(val description: String) {
    CREATED("Создан"),
    CONFIRMED("Подтвержден"),
    GOING("В пути"),
    DELIVERED("Доставлен"),
    CANCELED("Отменен");

    override fun toString(): String {
        return description
    }
} 