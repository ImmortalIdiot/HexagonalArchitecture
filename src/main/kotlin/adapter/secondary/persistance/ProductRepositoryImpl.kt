package adapter.secondary.persistance

import domain.model.Product
import domain.port.secondary.ProductRepository
import java.time.LocalDate

class ProductRepositoryImpl : ProductRepository {
    private val products = HashMap<String, Product>()

    init {
        addProduct(Product("1", "Творог", 200.0, LocalDate.now().plusDays(5)))
        addProduct(Product("2", "Курица", 500.0, LocalDate.now().minusDays(13)))
        addProduct(Product("3", "Молоко", 90.0, LocalDate.now().plusDays(7)))
        addProduct(Product("4", "Яйца", 120.0, LocalDate.now().plusDays(10)))
        addProduct(Product("5", "Лимонад", 50.0, LocalDate.now().plusDays(20)))
        addProduct(Product("6", "Пельмени", 250.0, LocalDate.now().plusDays(15)))
        addProduct(Product("7", "Селёдка", 400.0, LocalDate.now().minusDays(5)))
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