package com.api.nft.rabbitMQ

import com.api.nft.event.dto.NftResponse
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RabbitMQSender(
    private val rabbitTemplate: RabbitTemplate
) {

    fun nftSend(nft: NftResponse) {
        rabbitTemplate.convertAndSend("nftExchange", "", nft)
    }
}