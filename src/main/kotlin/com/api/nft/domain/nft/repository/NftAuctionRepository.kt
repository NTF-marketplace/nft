package com.api.nft.domain.nft.repository

import com.api.nft.domain.nft.NftAuction
import com.api.nft.enums.StatusType
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

interface NftAuctionRepository : R2dbcRepository<NftAuction,Long>  {
    fun findByNftId(nftId: Long) : Mono<NftAuction>

    @Query("UPDATE nft_auction SET status_type = :statusType WHERE nft_id = :nftId")
    fun updateAuction(nftId: Long, statusType: StatusType): Mono<Void>

    fun deleteByNftId(nftId: Long): Mono<Void>

}