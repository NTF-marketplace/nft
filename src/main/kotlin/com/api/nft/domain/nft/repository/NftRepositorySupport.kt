package com.api.nft.domain.nft.repository

import com.api.nft.controller.dto.NftMetadataResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface NftRepositorySupport {
    fun findByNftJoinMetadata(nftId: Long) : Mono<NftMetadataResponse>
    fun findAllByNftJoinMetadata(nftIds: List<Long>) : Flux<NftMetadataResponse>
}