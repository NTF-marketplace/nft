package com.api.nft.service.api

import com.api.nft.domain.nft.repository.NftListingRepository
import com.api.nft.service.dto.ListingResponse
import org.springframework.stereotype.Service

@Service
class NftListingService(
    private val nftListingRepository: NftListingRepository,
) {

    fun update(listing: ListingResponse) {
        // 있으면 update 없으면 저장 // 있으면 가격비교 후
        nftListingRepository.findByNftId(listing.nftId)
    }
}