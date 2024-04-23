package com.api.nft.domain.nft.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface NftRepositorySupport {
    fun findByNftJoinMetadata(nftId: Long) : Mono<NftMetadataDto>
    fun findAllByNftJoinMetadata(nftIds: List<Long>) : Flux<NftMetadataDto>
}