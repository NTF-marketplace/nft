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
        println("몇번 들어오는지 체크할게요")
        println("active : " + listing.active)
        nftListingService.update(listing).subscribe()
    }
    @RabbitListener(queues = ["listingCancelQueue"])
    fun listingCancelMessage(listing: ListingResponse){
//        println("ids: " + nftIds.toList())
        nftListingService.update(listing).subscribe()
    }
}