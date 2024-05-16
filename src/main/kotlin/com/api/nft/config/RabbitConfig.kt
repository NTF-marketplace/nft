package com.api.nft.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter

@Configuration
class RabbitConfig {
    @Bean
    fun nftQueue(): Queue {
        return Queue("nftQueue", true)
    }

    @Bean
    fun nftExchange(): DirectExchange {
        return DirectExchange("nftExchange")
    }

    @Bean
    fun bindingNftQueue(nftQueue: Queue, nftExchange: DirectExchange): Binding {
        return BindingBuilder.bind(nftQueue).to(nftExchange).with("nftRoutingKey")
    }

    @Bean
    fun jsonMessageConverter(): Jackson2JsonMessageConverter = Jackson2JsonMessageConverter()

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory, jsonMessageConverter: Jackson2JsonMessageConverter): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter = jsonMessageConverter
        return template
    }
}