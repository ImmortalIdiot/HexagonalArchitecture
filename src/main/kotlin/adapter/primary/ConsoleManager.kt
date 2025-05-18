package adapter.primary

import domain.model.OrderItem
import domain.model.OrderStatus
import domain.port.primary.OrderService
import java.util.*

class ConsoleManager(private val orderManagement: OrderService) {
    private val scanner = Scanner(System.`in`)
    private val random = Random()
    private var currentOrderId: String? = null
    private var currentOrderStatus: OrderStatus? = null

    fun start() {
        var choice: Int
        println("Запуск системы управления заказами...")
        do {
            showMainMenu()
            choice = readIntInput()
            scanner.nextLine()
            handleMainMenuChoice(choice)
        } while (choice != 0)
    }

    private fun showMainMenu() {
        println("\n===== Система управления заказами =====")
        if (currentOrderId != null) {
            println("Текущий заказ: #$currentOrderId | Статус - $currentOrderStatus")
        }
        println("1. Создать заказ")
        println("2. Выбрать существующий заказ")
        println("3. Показать все заказы")

        if (currentOrderId != null) {
            println("4. Добавить товар в текущий заказ")
            println("5. Удалить товар из текущего заказа")
            println("6. Изменить статус текущего заказа")
            println("7. Показать детали текущего заказа")
        }

        if (currentOrderStatus == OrderStatus.DELIVERED) {
            println("8. Проверить качество продуктов в заказе")
        }

        println("0. Выход")
        print("Выберите действие: ")
    }

    private fun handleMainMenuChoice(choice: Int) {
        when (choice) {
            0 -> println("Выход из программы...")
            1 -> createNewOrder()
            2 -> selectExistingOrder()
            3 -> showAllOrders()
            4 -> {
                if (currentOrderId != null) addProductToOrder()
                else showNoOrderSelectedMessage()
            }
            5 -> {
                if (currentOrderId != null) removeProductFromOrder()
                else showNoOrderSelectedMessage()
            }
            6 -> {
                if (currentOrderId != null) changeOrderStatus()
                else showNoOrderSelectedMessage()
            }
            7 -> {
                if (currentOrderId != null) showOrderDetails()
                else showNoOrderSelectedMessage()
            }
            8 -> {
                if (currentOrderStatus == OrderStatus.DELIVERED) checkProductsInOrder()
                else showNoOrderSelectedMessage()
            }
            else -> println("Неверный выбор. Попробуйте снова.")
        }
    }

    private fun createNewOrder() {
        val forecastProducts = orderManagement.findAllProducts()
        val products = ArrayList<OrderItem>()

        for (product in forecastProducts) {
            val quantity = 5 + random.nextInt(26)
            products.add(OrderItem(product, quantity))
        }

        currentOrderId = orderManagement.createOrder(products)
        currentOrderStatus = orderManagement.getOrderById(currentOrderId!!).status
        println("Создан новый заказ #$currentOrderId | Статус - $currentOrderStatus")
    }

    private fun selectExistingOrder() {
        val orders = orderManagement.getAllOrders()

        if (orders.isEmpty()) {
            println("Нет доступных заказов.")
            return
        }

        println("\n=== Доступные заказы ===")
        for (i in orders.indices) {
            val order = orders[i]
            println("${i + 1}. Заказ #${order.id} (${order.status})")
        }

        print("Выберите номер заказа (1-${orders.size}): ")
        val orderIndex = readIntInput() - 1
        scanner.nextLine()

        if (orderIndex >= 0 && orderIndex < orders.size) {
            currentOrderId = orders[orderIndex].id
            currentOrderStatus = orders[orderIndex].status
            println("Выбран заказ #$currentOrderId")
        } else {
            println("Некорректный выбор заказа.")
        }
    }

    private fun showAllOrders() {
        val orders = orderManagement.getAllOrders()

        if (orders.isEmpty()) {
            println("Нет доступных заказов.")
            return
        }

        println("\n=== Все заказы ===")
        for (order in orders) {
            println("Заказ #${order.id}")
            println("Статус: ${order.status}")
            println("Сумма: ${order.calculateTotalPrice()} руб.")
            println("Товары: ${order.products.size}")
            println("-----------------------")
        }
    }

    private fun addProductToOrder() {
        val products = orderManagement.findAllProducts()

        if (products.isEmpty()) {
            println("Нет доступных товаров.")
            return
        }

        println("\n=== Доступные товары ===")
        for (i in products.indices) {
            val product = products[i]
            println("${i + 1}. $product")
        }

        print("Выберите номер товара (1-${products.size}): ")
        val productIndex = readIntInput() - 1
        scanner.nextLine()

        if (productIndex < 0 || productIndex >= products.size) {
            println("Некорректный выбор товара.")
            return
        }

        print("Введите количество: ")
        val quantity = readIntInput()
        scanner.nextLine()

        try {
            val productId = products[productIndex].id
            orderManagement.addProductToOrder(currentOrderId!!, productId, quantity)
            println("Товар успешно добавлен в заказ.")
        } catch (e: Exception) {
            println("Ошибка при добавлении товара: ${e.message}")
        }
    }

    private fun removeProductFromOrder() {
        val orderOpt = orderManagement.getOrderById(currentOrderId!!)

        if (orderOpt.products.isEmpty()) {
            println("В заказе нет товаров.")
            return
        }

        println("\n=== Товары в заказе ===")
        for (i in orderOpt.products.indices) {
            println("${i + 1}. ${orderOpt.products[i]}")
        }

        print("Выберите номер товара для удаления (1-${orderOpt.products.size}): ")
        val itemIndex = readIntInput() - 1
        scanner.nextLine()

        try {
            orderManagement.removeProductFromOrder(currentOrderId!!, itemIndex)
            println("Товар успешно удален из заказа.")
        } catch (e: Exception) {
            println("Ошибка при удалении товара: ${e.message}")
        }
    }

    private fun changeOrderStatus() {
        val orderOpt = orderManagement.getOrderById(currentOrderId!!)
        val currentStatus = orderOpt.status

        lateinit var availableStatuses: List<OrderStatus>

        when {
            currentStatus == OrderStatus.CREATED -> {
                availableStatuses = listOf(OrderStatus.CONFIRMED, OrderStatus.CANCELED)
            }
            currentStatus == OrderStatus.CONFIRMED -> {
                availableStatuses = listOf(OrderStatus.GOING, OrderStatus.CANCELED)
            }
            currentStatus == OrderStatus.GOING -> {
                availableStatuses = listOf(OrderStatus.DELIVERED, OrderStatus.CANCELED)
            }
            currentStatus == OrderStatus.DELIVERED -> {
                availableStatuses = listOf()
            }
        }

        println("\nТекущий статус заказа: $currentStatus")

        if (availableStatuses.isEmpty()) {
            println("Заказ доставлен. Нет доступных статусов.")
        } else {
            println("Доступные статусы:")
            for (i in availableStatuses.indices) {
                println("${i + 1}. ${availableStatuses[i]}")
            }

            print("Выберите новый статус (1-${availableStatuses.size}): ")
            val statusIndex = readIntInput() - 1
            scanner.nextLine()

            if (statusIndex < 0 || statusIndex >= availableStatuses.size) {
                println("Некорректный выбор статуса.")
                return
            }

            val newStatus = availableStatuses[statusIndex]

            try {
                orderManagement.changeOrderStatus(currentOrderId!!, newStatus)
                currentOrderStatus = newStatus
                println("Статус заказа успешно изменен на: $newStatus")
            } catch (e: Exception) {
                println("Ошибка при изменении статуса: ${e.message}")
            }
        }

        if (currentOrderStatus == OrderStatus.CANCELED) {
            try {
                orderManagement.deleteOrder(currentOrderId!!)
                currentOrderId = null
                println("Заказ успешно удален")
            } catch (e: Exception) {
                println("Ошибка при удалении заказа: ${e.message}")
            }
        }
    }

    private fun showOrderDetails() {
        val orderOpt = orderManagement.getOrderById(currentOrderId!!)
        println("\n$orderOpt")
    }

    private fun checkProductsInOrder() {
        try {
            orderManagement.checkProductsDate(currentOrderId!!)
        } catch (e: Exception) {
            println("Ошибка: ${e.message}")
        }
    }

    private fun showNoOrderSelectedMessage() {
        println("Сначала необходимо создать или выбрать заказ.")
    }

    private fun readIntInput(): Int {
        return try {
            scanner.nextInt()
        } catch (e: Exception) {
            -1
        }
    }
}
