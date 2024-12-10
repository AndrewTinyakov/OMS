package me.andrewtinyakov.oms.order

import jakarta.persistence.*
import me.andrewtinyakov.oms.product.Product
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    val quantity: Int,

    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.PENDING,

    var updatedAt: LocalDateTime = LocalDateTime.now()
)


enum class OrderStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED
}