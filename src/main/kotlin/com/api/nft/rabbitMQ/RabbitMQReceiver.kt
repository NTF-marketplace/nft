package com.api.nft.rabbitMQ

import com.api.nft.service.api.NftListingService
import com.api.nft.service.dto.ListingResponse
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class RabbitMQReceiver(
    private val nftListingService: NftListingService,
) {
    @RabbitListener(queues = ["listingQueue"])
    fun listingMessage(listing: ListingResponse){
        nftListingService.update(listing).subscribe()
    }

    @RabbitListener(queues = ["listingCancelQueue"])
    fun listingCancelMessage(nftIds: List<Long>){
        println("ids: " + nftIds.toList())
        nftListingService.batchDelete(nftIds).subscribe()
    }
}