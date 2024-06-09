package com.api.nft.domain.nft.repository

import com.api.nft.domain.nft.NftListing
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

interface NftListingRepository : R2dbcRepository<NftListing,Long> {
    fun findByNftId(nftId: Long) : Mono<NftListing>
}