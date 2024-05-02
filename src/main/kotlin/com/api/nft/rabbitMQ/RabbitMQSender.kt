package com.api.nft.rabbitMQ

import com.api.nft.domain.nft.Nft
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RabbitMQSender(
    private val rabbitTemplate: RabbitTemplate
) {

    //TODO("리팩토링")
    //TODO("반환값 재정의")
    fun nftSend(nft: Long) {
        rabbitTemplate.convertAndSend("nftExchange", "nftRoutingKey", nft)
    }
}