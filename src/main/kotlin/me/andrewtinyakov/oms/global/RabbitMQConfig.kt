package me.andrewtinyakov.oms.global

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Bean
    fun queue() = Queue("orders.queue")

    @Bean
    fun exchange() = TopicExchange("orders.exchange")

    @Bean
    fun binding(queue: Queue, exchange: TopicExchange): Binding =
        BindingBuilder.bind(queue).to(exchange).with("orders.queue")

}
