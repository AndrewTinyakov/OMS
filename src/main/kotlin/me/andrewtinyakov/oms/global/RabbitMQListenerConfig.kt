package me.andrewtinyakov.oms.global

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQListenerConfig {
    @Bean
    fun containerFactory(
        connectionFactory: ConnectionFactory
    ): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setConcurrentConsumers(2)
        factory.setMaxConcurrentConsumers(5)
        factory.setDefaultRequeueRejected(false)
        return factory
    }
}