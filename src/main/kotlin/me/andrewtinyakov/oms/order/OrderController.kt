package me.andrewtinyakov.oms.order

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(
        @RequestParam productId: Long,
        @RequestParam quantity: Int
    ): ResponseEntity<Order> {
        val order = orderService.placeOrder(productId, quantity)
        return ResponseEntity.ok(order)
    }

    @GetMapping("/{orderId}/status")
    fun getOrderStatus(
        @PathVariable orderId: Long
    ): ResponseEntity<OrderStatus> {
        val status = orderService.getOrderStatus(orderId)
        return ResponseEntity.ok(status)
    }

}