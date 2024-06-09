package com.api.nft.domain.nft.repository

import com.api.nft.domain.nft.NftListing
import com.api.nft.enums.TokenType
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono
import java.math.BigDecimal

interface NftListingRepository : R2dbcRepository<NftListing,Long> {

    fun findByNftId(nftId: Long) : Mono<NftListing>

    @Query("UPDATE nft_listing SET price = :price, token_type = :tokenType WHERE nft_id = :nftId")
    fun updateListing(nftId: Long, price: BigDecimal, tokenType: TokenType): Mono<Void>

}