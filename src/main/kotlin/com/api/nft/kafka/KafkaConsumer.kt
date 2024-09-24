package com.api.nft.kafka

import com.api.nft.enums.OrderType
import com.api.nft.kafka.dto.SaleResponse
import com.api.nft.service.api.NftAuctionService
import com.api.nft.service.api.NftListingService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.Message
import org.springframework.stereotype.Service

@Service
class KafkaConsumer(
    private val objectMapper: ObjectMapper,
    private val nftListingService: NftListingService,
    private val nftAuctionService: NftAuctionService
) {
    @KafkaListener(topics = ["sale-topic"],
        groupId = "nft-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun consumeLedgerStatusEvents(message: Message<Any>) {
        val payload = message.payload

        if (payload is LinkedHashMap<*, *>) {
            val saleStatusRequest = objectMapper.convertValue(payload, SaleResponse::class.java)
            println("saleStatusRequest : " + saleStatusRequest)
            when(saleStatusRequest.orderType){
                OrderType.LISTING -> nftListingService.update(saleStatusRequest).subscribe()
                OrderType.AUCTION -> nftAuctionService.update(saleStatusRequest).subscribe()
            }

            // orderService.updateOrderStatus(ledgerStatusRequest).subscribe()
        }
    }

}