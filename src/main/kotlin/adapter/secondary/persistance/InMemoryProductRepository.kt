package adapter.secondary.persistance

import domain.model.Product
import domain.port.secondary.ProductRepository
import java.time.LocalDate

class InMemoryProductRepository : ProductRepository {
    private val products = HashMap<String, Product>()

    init {
        // Инициализация тестовыми данными (Прогноз потребностей)
        // Имитация просроченности некоторых продуктов
        addProduct(Product("P1", "Помидоры", 120.0, LocalDate.now().plusDays(5)))
        addProduct(Product("P2", "Куриное филе", 300.0, LocalDate.now().minusDays(13)))
        addProduct(Product("P3", "Молоко", 90.0, LocalDate.now().plusDays(7)))
        addProduct(Product("P4", "Яйца", 85.0, LocalDate.now().plusDays(10)))
        addProduct(Product("P5", "Картофель", 50.0, LocalDate.now().plusDays(20)))
        addProduct(Product("P6", "Сыр", 250.0, LocalDate.now().plusDays(15)))
        addProduct(Product("P7", "Лосось", 600.0, LocalDate.now().minusDays(5)))
    }

    private fun addProduct(product: Product) {
        products[product.id] = product
    }

    override fun findById(id: String): Product {
        return products[id]!!
    }

    override fun findAll(): List<Product> {
        return ArrayList(products.values)
    }
} 