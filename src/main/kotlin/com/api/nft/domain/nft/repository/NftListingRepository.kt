package com.api.nft.domain.nft.repository

import com.api.nft.domain.nft.NftListing
import com.api.nft.enums.StatusType
import com.api.nft.enums.TokenType
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import java.math.BigDecimal

interface NftListingRepository : ReactiveCrudRepository<NftListing,Long> {

    fun findByNftId(nftId: Long) : Mono<NftListing>

    @Query("UPDATE nft_listing SET status_type = :statusType WHERE nft_id = :nftId")
    fun updateListing(nftId: Long, statusType: StatusType): Mono<Void>

    fun deleteByNftId(nftId: Long): Mono<Void>

}