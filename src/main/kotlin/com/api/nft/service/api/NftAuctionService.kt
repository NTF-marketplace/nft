package com.api.nft.service.api

import com.api.nft.domain.nft.NftAuction
import com.api.nft.domain.nft.repository.NftAuctionRepository
import com.api.nft.enums.StatusType
import com.api.nft.service.dto.AuctionResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class NftAuctionService(
    private val nftAuctionRepository: NftAuctionRepository,
) {

    fun update(newAuction: AuctionResponse): Mono<Void> {
        return when (newAuction.statusType) {
            StatusType.RESERVATION -> {
                save(newAuction)
            }
            StatusType.ACTIVED -> {
                nftAuctionRepository.findByNftId(newAuction.nftId)
                    .flatMap {
                        nftAuctionRepository.updateAuction(nftId = newAuction.nftId, statusType = StatusType.AUCTION)
                    }
            }
            StatusType.RESERVATION_CANCEL, StatusType.CANCEL, StatusType.EXPIRED -> {
                nftAuctionRepository.findByNftId(newAuction.nftId)
                    .flatMap { nftAuction ->
                        nftAuctionRepository.deleteByNftId(nftAuction.nftId)
                    }

            }
            else -> Mono.empty()
        }
    }
    fun save(auction: AuctionResponse) : Mono<Void> {
        return nftAuctionRepository.save(
            NftAuction(
                id = auction.id,
                nftId =  auction.nftId,
                startingPrice = auction.startingPrice,
                tokenType = auction.tokenType,
                statusType = auction.statusType,
                createdDate = auction.createdDateTime,
                endDate = auction.endDateTime,
            )
        ).then()
    }
}