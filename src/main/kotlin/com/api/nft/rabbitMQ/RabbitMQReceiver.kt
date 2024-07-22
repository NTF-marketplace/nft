package com.api.nft.rabbitMQ

import com.api.nft.service.api.NftListingService
import com.api.nft.service.dto.ListingResponse
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class RabbitMQReceiver(
    private val nftListingService: NftListingService,
) {


    @RabbitListener(bindings = [QueueBinding(
    value = Queue(name = "", durable = "false", exclusive = "true", autoDelete = "true"),
    exchange = Exchange(value = "listingExchange", type = ExchangeTypes.FANOUT)
    )])
    fun listingMessage(listing: ListingResponse){
        println("active : " + listing.active)
        nftListingService.update(listing).subscribe()
    }

    @RabbitListener(bindings = [QueueBinding(
        value = Queue(name = "", durable = "false", exclusive = "true", autoDelete = "true"),
        exchange = Exchange(value = "listingCancelExchange", type = ExchangeTypes.FANOUT)
    )])
    fun listingCancelMessage(listing: ListingResponse){
        nftListingService.update(listing).subscribe()
    }
}