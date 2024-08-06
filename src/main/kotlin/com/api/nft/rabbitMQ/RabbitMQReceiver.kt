package com.api.nft.rabbitMQ

import com.api.nft.service.api.NftAuctionService
import com.api.nft.service.api.NftListingService
import com.api.nft.service.dto.ListingResponse
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import com.api.nft.service.dto.AuctionResponse as AuctionResponse

@Service
class RabbitMQReceiver(
    private val nftListingService: NftListingService,
    private val nftAuctionService: NftAuctionService
) {


    @RabbitListener(bindings = [QueueBinding(
    value = Queue(name = "", durable = "false", exclusive = "true", autoDelete = "true"),
    exchange = Exchange(value = "listingExchange", type = ExchangeTypes.FANOUT)
    )])
    fun listingMessage(listing: ListingResponse){
        nftListingService.update(listing).subscribe()
    }

     @RabbitListener(bindings = [QueueBinding(
         value = Queue(name = "", durable = "false", exclusive = "true", autoDelete = "true"),
         exchange = Exchange(value = "auctionExchange", type = ExchangeTypes.FANOUT)
     )])
     fun auctionMessage(auction: AuctionResponse){
         nftAuctionService.update(auction).subscribe()
     }
}