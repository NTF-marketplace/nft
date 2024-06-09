package com.api.nft.service.api

import com.api.nft.domain.nft.NftListing
import com.api.nft.domain.nft.repository.NftListingRepository
import com.api.nft.service.dto.ListingResponse
import com.api.nft.storage.PriceStorage
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.math.BigDecimal

@Service
class NftListingService(
    private val nftListingRepository: NftListingRepository,
    private val priceStorage: PriceStorage,
) {

    fun update(newListing: ListingResponse): Mono<NftListing> {
        return nftListingRepository.findByNftId(newListing.nftId)
            .flatMap { nftListing ->
                val currentPrice = priceStorage.get(nftListing.tokenType)?.multiply(nftListing.price) ?: BigDecimal.ZERO
                val newPrice = priceStorage.get(newListing.tokenType)?.multiply(newListing.price) ?: BigDecimal.ZERO
                if (currentPrice < newPrice) {
                    nftListingRepository.updateListing(nftListing.nftId, price = newListing.price,newListing.tokenType)
                        .thenReturn(nftListing.update(newListing.price, newListing.tokenType))
                } else {
                    Mono.just(nftListing)
                }
            }.switchIfEmpty { save(newListing) }
    }

    fun save(listing: ListingResponse) : Mono<NftListing> {
        return nftListingRepository.save(
            NftListing(
                nftId =  listing.nftId,
                price = listing.price,
                tokenType = listing.tokenType
            )
        )
    }

    fun batchDelete(nftIds: List<Long>): Mono<Void> {
        return nftListingRepository.deleteAllByNftIdIn(nftIds)
    }
}