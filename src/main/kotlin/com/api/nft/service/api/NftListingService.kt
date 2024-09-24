package com.api.nft.service.api

import com.api.nft.domain.nft.NftListing
import com.api.nft.domain.nft.repository.NftListingRepository
import com.api.nft.enums.StatusType
import com.api.nft.kafka.dto.SaleResponse
import com.api.nft.service.RedisService
import com.api.nft.service.dto.ListingResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class NftListingService(
    private val nftListingRepository: NftListingRepository,
    private val redisService: RedisService,
) {

    fun update(newListing: SaleResponse): Mono<Void> {
        return when (newListing.statusType) {
            StatusType.RESERVATION -> {
                save(newListing)
                    .then(redisService.updateToRedis(newListing.nftId))
            }
            StatusType.ACTIVED -> {
                nftListingRepository.findByNftId(newListing.nftId)
                    .flatMap { nftListing ->
                        nftListingRepository.updateListing(nftId = nftListing.nftId, statusType = StatusType.LISTING)
                    }
                    .then(redisService.updateToRedis(newListing.nftId))
            }
            StatusType.RESERVATION_CANCEL, StatusType.CANCEL, StatusType.EXPIRED, StatusType.LEDGER -> {
                nftListingRepository.findByNftId(newListing.nftId)
                    .flatMap { nftListing ->
                        nftListingRepository.deleteByNftId(nftListing.nftId)
                    }
                    .then(redisService.updateToRedis(newListing.nftId))
            }
            else -> Mono.empty()
        }
    }
    fun save(listing: SaleResponse) : Mono<NftListing> {
        return nftListingRepository.save(
            NftListing(
                id = listing.id,
                nftId =  listing.nftId,
                price = listing.price,
                chainType = listing.chainType,
                statusType = listing.statusType,
                createdDate = listing.createdDateTime,
                endDate = listing.endDateTime,
            )
        )
    }

}