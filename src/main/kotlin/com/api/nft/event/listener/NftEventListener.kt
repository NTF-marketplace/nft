package com.api.nft.event.listener

import com.api.nft.event.NftCreatedEvent
import com.api.nft.rabbitMQ.provider.RabbitMQProvider
import org.springframework.context.event.EventListener

class NftEventListener(private val provider: RabbitMQProvider) {

    @EventListener
    fun onNftCreated(event: NftCreatedEvent) {
        println("NFT Created: ${event.nft.id}")
        provider.sendNft(event.nft.id!!)
    }
}