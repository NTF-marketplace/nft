package com.api.nft.rabbitMQ.provider

import com.api.nft.properties.RabbitMQProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate

class RabbitMQProvider(
    private val rabbitTemplate: RabbitTemplate,
    private val rabbitMQProperties: RabbitMQProperties
) {

    fun sendNft(nftId: Long) {
        rabbitTemplate.convertAndSend(rabbitMQProperties.queues?.get("nft")?: throw IllegalAccessException("must be nft-url"), nftId)
        println("Sent nft: $nftId")
    }
}