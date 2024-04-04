package com.api.nft.domain.repository

import com.api.nft.domain.Nft
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface NftRepository : ReactiveCrudRepository<Nft,Long> {

    fun findByTokenAddressAndTokenId(tokenADdress: String, tokenId: String): Mono<Nft>
}