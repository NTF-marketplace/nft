package com.api.nft.event

import com.api.nft.rabbitMQ.RabbitMQSender
import org.springframework.context.event.EventListener

class NftEventListener(
    private val provider: RabbitMQSender
){

    @EventListener
    fun onNftCreated(event: NftCreatedEvent) {
        provider.nftSend(event.nft)
    }
}