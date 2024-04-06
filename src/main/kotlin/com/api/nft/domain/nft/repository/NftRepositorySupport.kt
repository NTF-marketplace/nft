package com.api.nft.domain.nft.repository

import reactor.core.publisher.Mono

interface NftRepositorySupport {
    fun findByNftJoinMetadata(nftId: Long) : Mono<NftMetadataDto>
}