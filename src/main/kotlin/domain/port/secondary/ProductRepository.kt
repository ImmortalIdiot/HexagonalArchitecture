package domain.port.secondary

import domain.model.Product

interface ProductRepository {
    fun findById(id: String): Product
    
    fun findAll(): List<Product>
} 