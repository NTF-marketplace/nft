package com.api.nft.domain.nft.repository

import com.api.nft.domain.nft.Nft
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface NftRepository : ReactiveCrudRepository<Nft,Long>, NftRepositorySupport {

    fun findByTokenAddressAndTokenId(tokenAddress: String, tokenId: String): Mono<Nft>
}