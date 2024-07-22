package com.api.nft.service.api

import com.api.nft.domain.nft.NftListing
import com.api.nft.domain.nft.repository.NftListingRepository
import com.api.nft.domain.nft.repository.NftRepository
import com.api.nft.service.RedisService
import com.api.nft.service.dto.ListingResponse
import com.api.nft.storage.PriceStorage
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.math.BigDecimal

@Service
class NftListingService(
    private val nftListingRepository: NftListingRepository,
    private val redisService: RedisService,
) {

    fun update(newListing: ListingResponse): Mono<Void> {
        return if (newListing.active) {
            println("why not save ? : " + newListing.active )
            save(newListing)
                .then(redisService.updateToRedis(newListing.nftId))
        } else {
            nftListingRepository.findByNftId(newListing.nftId)
                .flatMap { nftListing ->
                    nftListingRepository.deleteByNftId(nftListing.nftId)
                }
                .then(redisService.updateToRedis(newListing.nftId))
        }
    }

    fun save(listing: ListingResponse) : Mono<NftListing> {
        println("do you save logic ?")
        return nftListingRepository.save(
            NftListing(
                nftId =  listing.nftId,
                price = listing.price,
                tokenType = listing.tokenType
            )
        )
    }

}