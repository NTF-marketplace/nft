package com.api.nft.service.api

import com.api.nft.domain.nft.NftListing
import com.api.nft.domain.nft.repository.NftListingRepository
import com.api.nft.enums.StatusType
import com.api.nft.service.RedisService
import com.api.nft.service.dto.ListingResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class NftListingService(
    private val nftListingRepository: NftListingRepository,
    private val redisService: RedisService,
) {

    fun update(newListing: ListingResponse): Mono<Void> {
        return when (newListing.statusType) {
            StatusType.RESERVATION -> {
                save(newListing)
                    .then(redisService.updateToRedis(newListing.nftId))
            }
            StatusType.LISTING -> {
                println("update type LISTING")
                nftListingRepository.findByNftId(newListing.nftId)
                    .flatMap { nftListing ->
                        println("id : " + newListing.nftId)
                        println("statusType : " + newListing.statusType)
                        nftListingRepository.updateListing(nftId = nftListing.nftId, statusType = newListing.statusType)
                    }
                    .then(redisService.updateToRedis(newListing.nftId))
            }
            StatusType.RESERVATION_CANCEL, StatusType.CANCEL, StatusType.EXPIRED -> {
                nftListingRepository.findByNftId(newListing.nftId)
                    .flatMap { nftListing ->
                        nftListingRepository.deleteByNftId(nftListing.nftId)
                    }
                    .then(redisService.updateToRedis(newListing.nftId))
            }
        }
    }
    fun save(listing: ListingResponse) : Mono<NftListing> {
        return nftListingRepository.save(
            NftListing(
                nftId =  listing.nftId,
                price = listing.price,
                tokenType = listing.tokenType,
                statusType = listing.statusType,
                createdDate = listing.createdDateTime,
                endDate = listing.endDateTime,
            )
        )
    }

}