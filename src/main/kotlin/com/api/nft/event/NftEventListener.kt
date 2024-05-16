package com.api.nft.event

import com.api.nft.rabbitMQ.RabbitMQSender
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class NftEventListener(
    private val provider: RabbitMQSender
){

    @EventListener
    fun onNftCreated(event: NftCreatedEvent) {
        println("메세지 오는가?")
        provider.nftSend(event.nft)
    }
}