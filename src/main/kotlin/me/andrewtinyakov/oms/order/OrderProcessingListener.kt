package me.andrewtinyakov.oms.order

import me.andrewtinyakov.oms.product.ProductRepository
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderProcessingListenerImpl(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository
) : OrderProcessingListener {

    @RabbitListener(queues = ["orders.queue"])
    fun processOrder(orderId: Long) {
        val order = orderRepository.findById(orderId)
            .orElseThrow { RuntimeException("Order not found: $orderId") }

        order.status = OrderStatus.PROCESSING
        order.updatedAt = LocalDateTime.now()
        orderRepository.save(order)

        try {
            val product = order.product
            if (product.quantity < order.quantity) {
                throw RuntimeException("Insufficient stock for product: ${product.id}")
            }
            product.quantity -= order.quantity
            productRepository.save(product)

            order.status = OrderStatus.COMPLETED
        } catch (e: Exception) {
            order.status = OrderStatus.FAILED
        } finally {
            order.updatedAt = LocalDateTime.now()
            orderRepository.save(order)
        }
    }
}

interface OrderProcessingListener {
}