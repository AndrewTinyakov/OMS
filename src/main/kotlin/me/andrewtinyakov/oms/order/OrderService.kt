package me.andrewtinyakov.oms.order

import jakarta.transaction.Transactional
import me.andrewtinyakov.oms.product.ProductRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
@Transactional
class OrderServiceImpl(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val rabbitTemplate: RabbitTemplate
) : OrderService {

    override fun placeOrder(productId: Long, quantity: Int): Order {
        val product = productRepository.findById(productId)
            .orElseThrow { RuntimeException("Product not found") }

        if (product.quantity < quantity)
            throw RuntimeException("Insufficient stock")

        val order = orderRepository.save(
            Order(
                product = product,
                quantity = quantity,
                status = OrderStatus.PENDING
            )
        )

        // Send message to RabbitMQ
        rabbitTemplate.convertAndSend("orders.queue", order.id)
        return order
    }

    override fun getOrderStatus(orderId: Long): OrderStatus {
        val order = orderRepository.findById(orderId)
            .orElseThrow { RuntimeException("Order not found") }
        return order.status
    }

}

interface OrderService {
    fun placeOrder(productId: Long, quantity: Int): Order
    fun getOrderStatus(orderId: Long): OrderStatus
}